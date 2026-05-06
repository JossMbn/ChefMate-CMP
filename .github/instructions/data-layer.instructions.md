---
applyTo: "**/*DataSource*.kt,**/*Repository*.kt,**/*Dto.kt,**/*Mapper.kt"
---
# Data Layer Instructions
## Layer Responsibility
The data layer implements the contracts defined in the domain layer. It is the only layer
allowed to know about Supabase, Ktor, or any external dependency.
**Allowed dependencies**: Supabase-kt, Ktor, kotlinx.serialization, Koin
**Forbidden dependencies**: Compose, ViewModel, any UI framework
## Package Structure
The data layer is organised by **entity** (the type of data), not by generic technical
packages like `datasource/`, `repository/`, `mapper/`.
```
data/
└── {entity}/                         # e.g. recipe, collection, authentication
    ├── {Entity}RepositoryImpl.kt     # Repository implementation
    └── source/
        ├── remote/
        │   ├── {Entity}RemoteDataSource.kt
        │   ├── {Entity}RemoteDataSourceImpl.kt
        │   ├── dto/                  # @Serializable DTOs
        │   │   └── {Entity}Dto.kt
        │   ├── model/                # Enums for RPC functions, tables, columns
        │   │   ├── {Entity}RpcFunction.kt
        │   │   └── {Entity}Table.kt
        │   └── parameter/            # @Serializable RPC parameters
        │       └── Get{Entity}Parameter.kt
        └── cache/                    # Only if reactive sharing needed
            ├── {Entity}CacheDataSource.kt
            └── {Entity}CacheDataSourceImpl.kt
```
Each entity package is self-contained: everything needed to implement the domain contract
for that entity lives under one top-level folder.
---
## DTOs — Data Transfer Objects
DTOs represent the exact shape of data from Supabase. They live in `data/{entity}/source/remote/dto/`.
### Rules
- Always annotate with `@Serializable`
- Always annotate every field with `@SerialName("snake_case_name")` matching the Supabase column
- All fields should have default values where Supabase may return null
- Never add business logic to a DTO
- Never use a DTO outside the `data` layer — map to Domain model immediately
```kotlin
// data/{entity}/source/remote/dto/{Entity}Dto.kt
@Serializable
data class {Entity}Dto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
```
---
## Mappers
Mappers convert DTOs to Domain models. They live in `domain/{entity}/mapper/`.
This keeps the mapping knowledge within the domain entity that owns the target model.
### Rules
- Implement the `Mapper<Domain, Dto>` interface
- One mapper class per DTO/Domain pair
- Never perform mapping inline in a DataSource or Repository
- Mapping logic is the only business logic allowed in the data layer
```kotlin
// core/data/mapper/Mapper.kt (shared interface)
interface Mapper<out Domain, in Dto> {
    fun convert(input: Dto): Domain
}
// domain/{entity}/mapper/{Entity}Mapper.kt
class {Entity}Mapper : Mapper<{Entity}Domain, {Entity}Dto> {
    override fun convert(input: {Entity}Dto): {Entity}Domain {
        return {Entity}Domain(
            id = input.id,
            name = input.name
            // map remaining fields
        )
    }
}
```
For list decoding, use the existing extensions:
```kotlin
// Decodes a single object and maps it
postgrestResult.decodeAndMap(mapper = {Entity}Mapper())
// Decodes a list and maps each element
postgrestResult.decodeListAndMap(mapper = {Entity}Mapper())
```
---
## Remote DataSources
Remote DataSources make network calls to Supabase. They live in `data/{entity}/source/remote/`.
### Interface
```kotlin
// data/{entity}/source/remote/{Entity}RemoteDataSource.kt
interface {Entity}RemoteDataSource {
    suspend fun getItems(page: Int): Result<List<{Entity}Domain>>
    suspend fun createItem(name: String): Result<{Entity}Domain>
    suspend fun deleteItem(id: String): Result<Unit>
}
```
### Implementation Rules
- **Always** wrap Supabase calls in `safeExecution {}` — never bare try/catch
- Use `postgrest.rpc()` for complex queries or aggregations
- Use `postgrest.from().select/update/insert/delete` for simple CRUD only
- Pagination: always use `limit` + `offset` parameters via RPC
- Return `Result<T>` — never throw exceptions
- Never hold state — DataSources are stateless
```kotlin
// data/{entity}/source/remote/{Entity}RemoteDataSourceImpl.kt
class {Entity}RemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : {Entity}RemoteDataSource {
    companion object {
        private const val PAGINATION_LIMIT = 20
    }
    override suspend fun getItems(page: Int): Result<List<{Entity}Domain>> {
        val offset = page * PAGINATION_LIMIT
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = {Entity}RpcFunction.GetItems.functionName,
                parameters = Get{Entity}Parameter(limit = PAGINATION_LIMIT, offset = offset)
            ).decodeListAndMap(mapper = {Entity}Mapper())
        }
    }
    override suspend fun createItem(name: String): Result<{Entity}Domain> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = {Entity}RpcFunction.CreateItem.functionName,
                parameters = Create{Entity}Parameter(name = name)
            ).decodeAndMap(mapper = {Entity}Mapper())
        }
    }
}
```
### Supabase RPC Functions — Naming Convention
Centralise RPC function names and table names in enums inside `data/{entity}/source/remote/model/`:
```kotlin
// data/{entity}/source/remote/model/{Entity}RpcFunction.kt
enum class {Entity}RpcFunction(val functionName: String) {
    GetItems("get_{entities}"),
    CreateItem("create_{entity}"),
    DeleteItem("delete_{entity}")
}
// data/{entity}/source/remote/model/{Entity}Table.kt
enum class {Entity}Table(val tableName: String) {
    Items("{entities}")
}
```
### RPC Parameters
Parameters live in `data/{entity}/source/remote/parameter/`. Each RPC call has its own
`@Serializable` parameter class:
```kotlin
// data/{entity}/source/remote/parameter/Get{Entity}Parameter.kt
@Serializable
data class Get{Entity}Parameter(
    @SerialName("p_limit")
    val limit: Int,
    @SerialName("p_offset")
    val offset: Int
)
```
---
## CacheEngine — Internal Building Block
`CacheEngine<T>`, `CacheConfiguration`, and `CacheError` live in `core/data/cache/`.
They are the shared infrastructure used by every `{Entity}CacheDataSourceImpl`.
### Key rules
- **Never injectable** — `CacheEngine` is always a `private val` inside its `CacheDataSourceImpl`.
  It never appears in any Koin module.
- **Never used directly** outside a `*CacheDataSourceImpl` — the repository only knows the
  `*CacheDataSource` interface.
- One `CacheEngine` instance per `CacheDataSourceImpl`. Each Impl owns its lifecycle.
### CacheEngine API
| Method | Description |
|---|---|
| `get(key)` | Snapshot read — returns `Result.failure(CacheError.NotFound/Expired)` on miss |
| `getAll()` | Returns all non-expired entries as a `Result<List<T>>` |
| `observe(key)` | `Flow<T?>` — emits `null` when absent or expired |
| `observeAll()` | `Flow<Map<String, T>>` — emits on every write |
| `put(key, value)` | Insert / replace a single entry (timestamped now) |
| `putAll(map)` | Atomically insert / replace multiple entries |
| `remove(key)` | Remove a single entry (no-op if absent) |
| `clear()` | Remove all entries (call on logout or full invalidation) |
### CacheConfiguration
```kotlin
CacheConfiguration(
    cacheDurationMs = Duration.INFINITE, // entries never expire unless explicitly cleared
    isEnabled = true,                    // set to false to force every read to fall through
    isDebug = false
)
```
Use `Duration.INFINITE` unless you have a concrete business requirement for TTL-based expiry.
### CacheError and the fallback-to-remote pattern
Every `CacheError` is a `Throwable`, so repositories use `recoverCatching` to fall back
to the remote source transparently:
```kotlin
override suspend fun getItemDetails(
    itemId: String,
    page: Int
): Result<{Entity}DetailsDomain> {
    return cacheDataSource.getItemDetails(itemId = itemId, page = page)
        .recoverCatching { error ->
            // Only recover from cache misses — rethrow network/domain errors
            if (!error.isCacheError()) throw error
            fetchAndCacheItemDetails(itemId = itemId, page = page).getOrThrow()
        }
}
```
`isCacheError()` is an extension on `Throwable` declared in `core/data/cache/CacheError.kt`.
Always use it instead of catching `CacheError` directly.
---
## Cache DataSources — Reactive Cache
Use a `{Entity}CacheDataSource` **only** when multiple ViewModels must observe the same
data and receive automatic updates when it changes (e.g. a list on a parent screen must
update when a child screen modifies an item's membership).
Cache DataSources live in `data/{entity}/source/cache/`.
### When to use
✅ Shared mutable data observed by 2+ ViewModels simultaneously
✅ Data that changes via user action in a child screen and must reflect in parent screens
❌ Data only used by a single screen
❌ Data that is always fetched fresh from the network
### Interface
Define the public contract in `data/{entity}/source/cache/{Entity}CacheDataSource.kt`.
Expose only what the repositories need — no `CacheEngine` types leak through.
```kotlin
// data/{entity}/source/cache/{Entity}CacheDataSource.kt
interface {Entity}CacheDataSource {
    // Snapshot reads
    fun getItem(itemId: String): Result<{Entity}Domain>
    fun getAllItems(): Result<List<{Entity}Domain>>
    // Reactive observation
    fun observeItem(itemId: String): Flow<{Entity}Domain?>
    fun observeAllItems(): Flow<List<{Entity}Domain>>
    // Writes
    suspend fun cacheItem(item: {Entity}Domain)
    suspend fun cacheItems(items: List<{Entity}Domain>)
    suspend fun updateItem(item: {Entity}Domain)
    // Invalidation
    fun invalidate(itemId: String)
    fun invalidateAll()
}
```
### Implementation
The `Impl` lives in the same `cache/` package. The `CacheEngine` is intentionally
**not injectable** — the Impl owns its lifecycle.
```kotlin
// data/{entity}/source/cache/{Entity}CacheDataSourceImpl.kt
class {Entity}CacheDataSourceImpl : {Entity}CacheDataSource {
    /**
     * The [CacheEngine] is intentionally NOT injectable — this Impl owns its lifecycle.
     * Use [Duration.INFINITE] unless entries must expire after a known business TTL.
     */
    private val engine = CacheEngine<{Entity}Domain>(
        configuration = CacheConfiguration(cacheDurationMs = Duration.INFINITE)
    )
    override fun getItem(itemId: String): Result<{Entity}Domain> =
        engine.get(key = itemId)
    override fun getAllItems(): Result<List<{Entity}Domain>> =
        engine.getAll()
    override fun observeItem(itemId: String): Flow<{Entity}Domain?> =
        engine.observe(key = itemId)
    override fun observeAllItems(): Flow<List<{Entity}Domain>> =
        engine.observeAll().map { it.values.toList() }
    override suspend fun cacheItem(item: {Entity}Domain) =
        engine.put(key = item.id, value = item)
    override suspend fun cacheItems(items: List<{Entity}Domain>) =
        engine.putAll(items.associateBy { it.id })
    override suspend fun updateItem(item: {Entity}Domain) =
        engine.put(key = item.id, value = item)
    override fun invalidate(itemId: String) =
        engine.remove(key = itemId)
    override fun invalidateAll() =
        engine.clear()
}
```
### Koin declaration
`CacheDataSourceImpl` instances are declared as `single {}` because their internal
`CacheEngine` holds shared reactive state. The `CacheEngine` itself remains invisible
to the DI graph — it is never registered in any Koin module.
```kotlin
singleOf(::{Entity}CacheDataSourceImpl).bind<{Entity}CacheDataSource>()
```
---
## Repositories
Repositories coordinate between DataSources and expose data to the domain layer.
The implementation lives in `data/{entity}/{Entity}RepositoryImpl.kt`, co-located with
its data sources. The interface lives in `domain/{entity}/repository/{Entity}Repository.kt`.
### Pattern A — Simple (no shared reactive state)
Use `suspend fun` exclusively. The ViewModel triggers a load, gets a `Result`, updates state.
```kotlin
// data/{entity}/{Entity}RepositoryImpl.kt
class {Entity}RepositoryImpl(
    private val remoteDataSource: {Entity}RemoteDataSource
) : {Entity}Repository {
    override suspend fun getItemDetails(id: String): Result<{Entity}Domain> {
        return remoteDataSource.getItemDetails(id)
    }
    override suspend fun createItem(item: New{Entity}Domain): Result<{Entity}Domain> {
        return remoteDataSource.createItem(item)
    }
}
```
### Pattern B — Reactive (with CacheDataSource, for shared state)
Expose a `Flow` from the cache. Load from network via a separate `suspend fun`.
Mutations update the network first, then the cache on success.
```kotlin
// data/{entity}/{Entity}RepositoryImpl.kt
class {Entity}RepositoryImpl(
    private val remoteDataSource: {Entity}RemoteDataSource,
    private val cacheDataSource: {Entity}CacheDataSource
) : {Entity}Repository {
    // Reactive stream — ViewModels observe this
    override fun observeItems(): Flow<List<{Entity}Domain>> =
        cacheDataSource.observeAllItems()
            .onStart {
                cacheDataSource.getAllItems()
                    .getOrNull()
                    ?.takeIf { it.isNotEmpty() }
                    ?: loadItems(page = 0)
            }
    // Trigger a network load and feed the cache
    override suspend fun loadItems(page: Int): Result<Unit> {
        return remoteDataSource.getItems(page)
            .onSuccess { cacheDataSource.cacheItems(it) }
            .map { }
    }
    // Mutation — update network, then update cache on success
    override suspend fun deleteItem(itemId: String): Result<Unit> {
        return remoteDataSource.deleteItem(itemId)
            .onSuccess { cacheDataSource.invalidate(itemId) }
    }
}
```
---
## safeExecution — Error Handling Contract
The `safeExecution` extension on `SupabaseClient` is declared in `core/network/`.
Never replicate or bypass it.
```kotlin
suspend fun <T> SupabaseClient.safeExecution(block: suspend SupabaseClient.() -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e // always rethrow
    } catch (e: RestException) {
        Result.failure(e.toError())
    } catch (_: HttpRequestTimeoutException) {
        Result.failure(NetworkError.TimeoutError())
    } catch (_: HttpRequestException) {
        Result.failure(NetworkError.NetworkConnectionError())
    } catch (_: SerializationException) {
        Result.failure(NetworkError.SerializationError())
    } catch (e: Exception) {
        Result.failure(NetworkError.Unknown(e.message))
    }
```
**Error hierarchy** lives in `core/network/error/`:
```kotlin
sealed class NetworkError(message: String? = null) : Exception(message) {
    class TimeoutError : NetworkError()
    class NetworkConnectionError : NetworkError()
    class SerializationError : NetworkError()
    class BadRequest(message: String?) : NetworkError(message)
    class Unauthorized : NetworkError()
    class Forbidden : NetworkError()
    class NotFound : NetworkError()
    class ValidationError(message: String?) : NetworkError(message)
    class TooManyRequests : NetworkError()
    class ServerError : NetworkError()
    class Unknown(message: String?) : NetworkError(message)
}
sealed class AuthenticationError(message: String? = null) : Exception(message) {
    class InvalidCredentials : AuthenticationError()
    class EmailNotConfirmed : AuthenticationError()
    class UserAlreadyExists : AuthenticationError()
    class WeakPassword : AuthenticationError()
    class RateLimitExceeded : AuthenticationError()
}
```
---
## Checklist Before Submitting Data Layer Code
- [ ] All DTOs are `@Serializable` with `@SerialName` on every field
- [ ] DTOs live in `data/{entity}/source/remote/dto/`
- [ ] Mapping from DTO to Domain happens in a dedicated `Mapper` class in `domain/{entity}/mapper/`
- [ ] All Supabase calls use `safeExecution {}`
- [ ] `Result<T>` is returned and not swallowed
- [ ] `CancellationException` is never caught (it's rethrown in `safeExecution`)
- [ ] RPC function names are centralised in an enum in `data/{entity}/source/remote/model/`
- [ ] RPC parameters live in `data/{entity}/source/remote/parameter/`
- [ ] `CacheDataSource` (interface + Impl) created only when reactive state sharing is needed
- [ ] `CacheEngine` kept as a `private val` inside the `Impl` — never registered in Koin
- [ ] `CacheDataSourceImpl` declared as `singleOf(...).bind<Interface>()` in the Koin module
- [ ] `isCacheError()` used in repositories for cache-miss fallback (`recoverCatching`)
- [ ] Repository exposes `Flow` only when reactive state sharing is needed
