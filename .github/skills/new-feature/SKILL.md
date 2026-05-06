---
name: new-feature
description: >-
  Scaffolds a complete ChefMate feature with all layers: domain (models, repository interface,
  use cases), data (DTOs, mappers, datasources, repository impl), presentation (navigation,
  screen, viewmodel, previews), and Koin module. Use when asked to "create a feature",
  "scaffold the feature", "add the {name} feature", or "new feature for".
---

# Skill: New Feature

Scaffolds a complete vertical slice for a new ChefMate feature following Clean Architecture + MVI.

## Complete Package Structure

```
feature/{featureName}/
├── domain/
│   ├── model/
│   │   └── {Name}Domain.kt
│   ├── repository/
│   │   └── {Name}Repository.kt
│   └── usecase/
│       ├── Get{Name}UseCase.kt
│       └── Create{Name}UseCase.kt      ← add as many as needed
├── data/
│   ├── dto/
│   │   └── {Name}Dto.kt
│   ├── mapper/
│   │   └── {Name}Mapper.kt
│   ├── datasource/
│   │   ├── remote/
│   │   │   ├── {Name}RemoteDataSource.kt
│   │   │   └── {Name}RemoteDataSourceImpl.kt
│   │   └── cache/                      ← only if reactive sharing needed
│   │       ├── {Name}CacheDataSource.kt
│   │       └── {Name}CacheDataSourceImpl.kt
│   └── repository/
│       └── {Name}RepositoryImpl.kt
├── presentation/
│   ├── navigation/
│   │   └── {Name}Navigation.kt
│   ├── screen/
│   │   ├── {Name}Root.kt
│   │   └── {Name}ViewModel.kt
│   └── preview/
│       └── {Name}Preview.kt
└── {Feature}Module.kt                  ← Koin module
```

---

## Creation Order (follow this sequence)

### 1. Domain Models

Create the pure Kotlin domain model(s). No annotations.

```kotlin
// domain/model/{Name}Domain.kt
data class {Name}Domain(
    val id: String,
    // fields reflecting the business concept
)
```

### 2. Repository Interface

Define what the feature needs from the data layer.

```kotlin
// domain/repository/{Name}Repository.kt
interface {Name}Repository {
    // suspend fun for one-shot operations
    suspend fun get{Name}(id: String): Result<{Name}Domain>
    suspend fun create{Name}(/* params */): Result<{Name}Domain>
    suspend fun delete{Name}(id: String): Result<Unit>

    // Flow only if reactive sharing between ViewModels is required
    // val {names}: Flow<List<{Name}Domain>>
    // suspend fun load{Names}(): Result<Unit>
}
```

### 3. Use Case Interfaces + Implementations

One UseCase per business operation.

```kotlin
// domain/usecase/Get{Name}UseCase.kt
interface Get{Name}UseCase {
    suspend operator fun invoke(id: String): Result<{Name}Domain>
}

class Get{Name}UseCaseImpl(
    private val repository: {Name}Repository
) : Get{Name}UseCase {
    override suspend fun invoke(id: String): Result<{Name}Domain> =
        repository.get{Name}(id)
}
```

```kotlin
// domain/usecase/Create{Name}UseCase.kt
interface Create{Name}UseCase {
    suspend operator fun invoke(/* params */): Result<{Name}Domain>
}

class Create{Name}UseCaseImpl(
    private val repository: {Name}Repository
) : Create{Name}UseCase {
    override suspend fun invoke(/* params */): Result<{Name}Domain> {
        // Business validation here
        return repository.create{Name}(/* params */)
    }
}
```

### 4. DTO

```kotlin
// data/dto/{Name}Dto.kt
@Serializable
data class {Name}Dto(
    val id: String,
    @SerialName("field_name")
    val fieldName: String,
    @SerialName("created_at")
    val createdAt: String
)
```

### 5. Mapper

```kotlin
// data/mapper/{Name}Mapper.kt
class {Name}Mapper : Mapper<{Name}Domain, {Name}Dto> {
    override fun convert(input: {Name}Dto): {Name}Domain {
        return {Name}Domain(
            id = input.id,
            // map fields
        )
    }
}
```

### 6. Remote DataSource Interface + Implementation

```kotlin
// data/datasource/remote/{Name}RemoteDataSource.kt
interface {Name}RemoteDataSource {
    suspend fun get{Name}(id: String): Result<{Name}Domain>
    suspend fun create{Name}(/* params */): Result<{Name}Domain>
    suspend fun delete{Name}(id: String): Result<Unit>
}

// data/datasource/remote/{Name}RemoteDataSourceImpl.kt
class {Name}RemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : {Name}RemoteDataSource {

    override suspend fun get{Name}(id: String): Result<{Name}Domain> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = {Name}RpcFunction.Get{Name}.functionName,
                parameters = Get{Name}Parameter(id = id)
            ).decodeAndMap(mapper = {Name}Mapper())
        }
    }

    override suspend fun create{Name}(/* params */): Result<{Name}Domain> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = {Name}RpcFunction.Create{Name}.functionName,
                parameters = Create{Name}Parameter(/* params */)
            ).decodeAndMap(mapper = {Name}Mapper())
        }
    }

    override suspend fun delete{Name}(id: String): Result<Unit> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = {Name}RpcFunction.Delete{Name}.functionName,
                parameters = Delete{Name}Parameter(id = id)
            )
        }
    }
}

// RPC function names
enum class {Name}RpcFunction(val functionName: String) {
    Get{Name}("get_{name}"),
    Create{Name}("create_{name}"),
    Delete{Name}("delete_{name}")
}
```

### 7. Cache DataSource (only if reactive sharing needed)

Create only if multiple ViewModels observe the same data simultaneously.
See `data-layer.instructions.md` for the full CacheEngine pattern.

**Interface** — defines the public contract, no `CacheEngine` types exposed:

```kotlin
// data/datasource/cache/{Name}CacheDataSource.kt
interface {Name}CacheDataSource {
    // Snapshot reads
    fun get{Name}(id: String): Result<{Name}Domain>
    fun getAll{Names}(): Result<List<{Name}Domain>>
    // Reactive observation
    fun observe{Name}(id: String): Flow<{Name}Domain?>
    fun observeAll{Names}(): Flow<List<{Name}Domain>>
    // Writes
    suspend fun cache{Name}(item: {Name}Domain)
    suspend fun cache{Names}(items: List<{Name}Domain>)
    // Invalidation
    fun invalidate(id: String)
    fun invalidateAll()
}
```

**Implementation** — owns its `CacheEngine`, never injectable:

```kotlin
// data/datasource/cache/{Name}CacheDataSourceImpl.kt
class {Name}CacheDataSourceImpl : {Name}CacheDataSource {

    /**
     * The [CacheEngine] is intentionally NOT injectable — this Impl owns its lifecycle.
     */
    private val engine = CacheEngine<{Name}Domain>(
        configuration = CacheConfiguration(cacheDurationMs = Duration.INFINITE)
    )

    override fun get{Name}(id: String): Result<{Name}Domain> = engine.get(key = id)
    override fun getAll{Names}(): Result<List<{Name}Domain>> = engine.getAll()
    override fun observe{Name}(id: String): Flow<{Name}Domain?> = engine.observe(key = id)
    override fun observeAll{Names}(): Flow<List<{Name}Domain>> =
        engine.observeAll().map { it.values.toList() }
    override suspend fun cache{Name}(item: {Name}Domain) = engine.put(key = item.id, value = item)
    override suspend fun cache{Names}(items: List<{Name}Domain>) =
        engine.putAll(items.associateBy { it.id })
    override fun invalidate(id: String) = engine.remove(key = id)
    override fun invalidateAll() = engine.clear()
}
```

### 8. Repository Implementation

```kotlin
// data/repository/{Name}RepositoryImpl.kt
class {Name}RepositoryImpl(
    private val remoteDataSource: {Name}RemoteDataSource
) : {Name}Repository {

    override suspend fun get{Name}(id: String): Result<{Name}Domain> =
        remoteDataSource.get{Name}(id)

    override suspend fun create{Name}(/* params */): Result<{Name}Domain> =
        remoteDataSource.create{Name}(/* params */)

    override suspend fun delete{Name}(id: String): Result<Unit> =
        remoteDataSource.delete{Name}(id)
}
```

### 9. Koin Module

```kotlin
// {Feature}Module.kt
val {feature}Module = module {
    // Data sources
    single<{Name}RemoteDataSource> { {Name}RemoteDataSourceImpl(supabaseClient = get()) }
    // If reactive cache needed — CacheEngine is a private val inside the Impl, never injected:
    // singleOf(::{Name}CacheDataSourceImpl).bind<{Name}CacheDataSource>()

    // Repository
    single<{Name}Repository> {
        {Name}RepositoryImpl(remoteDataSource = get())
        // {Name}RepositoryImpl(remoteDataSource = get(), cacheDataSource = get())
    }

    // Use cases
    single<Get{Name}UseCase> { Get{Name}UseCaseImpl(repository = get()) }
    single<Create{Name}UseCase> { Create{Name}UseCaseImpl(repository = get()) }

    // ViewModels — use viewModelOf for constructor injection
    viewModelOf(::{Name}ViewModel)
}
```

### 10. Register module in AppModule

```kotlin
// core/di/AppModule.kt
val appModule = module {
    includes(
        // existing modules...
        {feature}Module
    )
}
```

### 11. Screen files

Use the `/new-screen` skill to generate all presentation layer files.

### 12. Navigation registration

Add the new route to the root NavHost or appropriate navigation graph:

```kotlin
{nameCamelCase}Page(controller = navController)
```

---

## Feature Checklist

### Domain
- [ ] Domain model(s) — pure Kotlin, no annotations
- [ ] Repository interface — `suspend fun` and/or `Flow` as appropriate
- [ ] UseCase interfaces + implementations (one per business operation)
- [ ] Business validation in UseCases before delegating to repository

### Data
- [ ] DTO(s) — `@Serializable`, all fields have `@SerialName`
- [ ] Mapper class implementing `Mapper<Domain, Dto>`
- [ ] Remote DataSource interface + implementation
- [ ] All Supabase calls wrapped in `safeExecution {}`
- [ ] RPC function names in an enum (no inline strings)
- [ ] Cache DataSource interface + Impl (only if reactive sharing justified)
  - [ ] `CacheEngine` is a `private val` inside the Impl, never in Koin
  - [ ] `CacheDataSourceImpl` declared as `singleOf(...).bind<Interface>()` in Koin
- [ ] Repository implementation

### Koin
- [ ] Feature module file created
- [ ] DataSources declared as `single {}`
- [ ] Repository declared as `single {}`
- [ ] UseCases declared as `single {}`
- [ ] ViewModels declared with `viewModelOf()`
- [ ] Module registered in `AppModule`

### Presentation
- [ ] Navigation file (Route, Navigator, NavGraphBuilder extension)
- [ ] Root composable (State, Action, Event contracts + Root, Page, Content)
- [ ] ViewModel
- [ ] Preview file with `@PreviewLightDark` and `PreviewParameterProvider`
- [ ] Screen registered in navigation graph

### Quality
- [ ] No business logic in Composables
- [ ] `collectAsStateWithLifecycle()` used (never `collectAsState()`)
- [ ] All strings in resources (no hardcoded strings in UI)
- [ ] All touch targets ≥ 48.dp
- [ ] `contentDescription` on all images and icon-only buttons
