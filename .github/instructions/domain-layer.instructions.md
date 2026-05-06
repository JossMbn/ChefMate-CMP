---
applyTo: "**/*UseCase*.kt,**/*Domain.kt,**/*Repository.kt"
---

# Domain Layer Instructions

## Layer Responsibility

The domain layer is the heart of the application. It contains the business rules and is
completely independent of any framework, library, or platform.

**Zero allowed external dependencies**: no Android, no Compose, no Supabase, no Ktor, no Koin.
**Only allowed**: Kotlin stdlib, kotlinx.coroutines, kotlinx-collections-immutable (if needed).

The domain layer defines **what** the app does. The data layer defines **how** it does it.

---

## Domain Models

Domain models are pure Kotlin data classes. They live in `domain/model/`.

### Rules

- Plain `data class` — no `@Serializable`, no `@Entity`, no framework annotation of any kind
- Immutable — all properties are `val`
- Use Kotlin types only (`String`, `Int`, `Boolean`, `List<T>`, etc.)
- May contain computed properties and simple validation logic
- Named with the `Domain` suffix to distinguish from DTOs

```kotlin
data class CollectionDomain(
    val id: String,
    val name: String,
    val systemType: CollectionSystemType?,
    val recipeCount: Int,
    val firstRecipeImageUrls: List<String>
)

data class RecipeDomain(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val ingredients: List<IngredientDomain>,
    val steps: List<StepDomain>,
    val collectionIds: List<String>,
    val createdAt: String
) {
    val hasImage: Boolean get() = imageUrl != null
    val ingredientCount: Int get() = ingredients.size
}

enum class CollectionSystemType(val value: String) {
    Favourites("favourites"),
    RecentlyViewed("recently_viewed");

    companion object {
        fun fromValue(value: String?): CollectionSystemType? =
            entries.firstOrNull { it.value == value }
    }
}
```

---

## Repository Interfaces

Repository interfaces define the data contract from the domain's perspective.
They live in `domain/repository/`.

### Rules

- Interfaces only — no implementation code
- Method names reflect business intent, not technical operations
  (`getCollections` not `fetchFromSupabase`)
- Use `suspend fun` for one-shot operations
- Use `Flow<T>` when the domain requires reactive observation of shared state
- Always return `Result<T>` for failable operations
- Never import `SupabaseClient`, `HttpClient`, or any data-layer type

```kotlin
// Simple repository — suspend only
interface RecipeRepository {
    suspend fun getRecipeDetails(id: String): Result<RecipeDomain>
    suspend fun createRecipe(recipe: NewRecipeDomain): Result<RecipeDomain>
    suspend fun updateRecipe(recipe: RecipeDomain): Result<RecipeDomain>
    suspend fun deleteRecipe(id: String): Result<Unit>
    suspend fun searchRecipes(query: String): Result<List<RecipeDomain>>
}

// Reactive repository — Flow for shared state
interface CollectionRepository {
    val collections: Flow<List<CollectionDomain>>
    suspend fun loadCollections(page: Int): Result<Unit>
    suspend fun createCollection(name: String): Result<CollectionDomain>
    suspend fun deleteCollection(id: String): Result<Unit>
    suspend fun updateCollection(id: String, newName: String): Result<CollectionDomain>
    suspend fun moveRecipeToCollections(recipeId: String, collectionIds: List<String>): Result<Unit>
}
```

---

## Use Cases

Use Cases encapsulate a single business operation. They live in `domain/usecase/`.

### Rules

- **Every UseCase has an interface + implementation** — never a class alone
- The interface exposes `operator fun invoke(...)` — makes the UseCase callable as a function
- `suspend operator fun invoke` for asynchronous operations
- `operator fun invoke` returning `Flow<T>` for reactive use cases
- One UseCase = one responsibility (Single Responsibility Principle)
- UseCases may compose other UseCases but never directly call DataSources
- Always return `Result<T>` for failable operations
- Named with a verb + noun + `UseCase` suffix: `CreateCollectionUseCase`, `ObserveCollectionsUseCase`

### Suspend UseCase (most common)

```kotlin
interface CreateCollectionUseCase {
    suspend operator fun invoke(name: String): Result<CollectionDomain>
}

class CreateCollectionUseCaseImpl(
    private val repository: CollectionRepository
) : CreateCollectionUseCase {
    override suspend fun invoke(name: String): Result<CollectionDomain> {
        // Business validation before delegating to the repository
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Collection name cannot be blank"))
        }
        return repository.createCollection(name.trim())
    }
}
```

### Reactive UseCase (observe shared state)

```kotlin
interface ObserveCollectionsUseCase {
    operator fun invoke(): Flow<List<CollectionDomain>>
}

class ObserveCollectionsUseCaseImpl(
    private val repository: CollectionRepository
) : ObserveCollectionsUseCase {
    override fun invoke(): Flow<List<CollectionDomain>> = repository.collections
}
```

### Load trigger UseCase (kick off a network fetch)

```kotlin
interface LoadCollectionsUseCase {
    suspend operator fun invoke(page: Int): Result<Unit>
}

class LoadCollectionsUseCaseImpl(
    private val repository: CollectionRepository
) : LoadCollectionsUseCase {
    override suspend fun invoke(page: Int): Result<Unit> =
        repository.loadCollections(page)
}
```

### UseCase composing other UseCases

```kotlin
interface MoveRecipeToCollectionsUseCase {
    suspend operator fun invoke(recipeId: String, collectionIds: List<String>): Result<Unit>
}

class MoveRecipeToCollectionsUseCaseImpl(
    private val collectionRepository: CollectionRepository,
    private val recipeRepository: RecipeRepository
) : MoveRecipeToCollectionsUseCase {
    override suspend fun invoke(recipeId: String, collectionIds: List<String>): Result<Unit> {
        // Business rule: cannot move to an empty list
        if (collectionIds.isEmpty()) {
            return Result.failure(IllegalArgumentException("Must select at least one collection"))
        }
        return collectionRepository.moveRecipeToCollections(recipeId, collectionIds)
    }
}
```

---

## Result<T> Usage

- All failable operations return `Result<T>`
- Business validation failures use `Result.failure(IllegalArgumentException(...))`
- Never throw from a UseCase — always wrap in `Result.failure`
- Compose `Result` with `.map {}`, `.flatMap {}`, `.onSuccess {}`, `.onFailure {}`

```kotlin
// ✅ Correct — chain Results without throwing
override suspend fun invoke(name: String): Result<CollectionDomain> {
    return repository.createCollection(name)
        .map { domain ->
            // post-process if needed
            domain
        }
        .onFailure { error ->
            // log if needed — do not swallow
            println("CreateCollection failed: ${error.message}")
        }
}
```

---

## Flow<Result<T>> for Reactive Failable Streams

When a repository exposes a `Flow` that can fail (e.g., remote + cache combination),
use `Flow<Result<T>>`. When the cache is the sole source and always valid, use `Flow<T>`.

```kotlin
// Cache-only — always valid data — Flow<T>
val collections: Flow<List<CollectionDomain>>

// Could fail (e.g. remote refresh embedded in stream) — Flow<Result<T>>
val syncedCollections: Flow<Result<List<CollectionDomain>>>
```

For ChefMate, since cache (`InMemoryDataSource`) is always valid once populated,
prefer `Flow<T>` from repositories and `suspend fun load*(): Result<Unit>` separately.

---

## Naming Conventions for Use Cases

| Operation | UseCase Name |
|---|---|
| Load / trigger network fetch | `Load{Name}UseCase` |
| Observe reactive stream | `Observe{Name}UseCase` |
| Create | `Create{Name}UseCase` |
| Update | `Update{Name}UseCase` |
| Delete | `Delete{Name}UseCase` |
| Search | `Search{Name}UseCase` |
| Move / Transfer | `Move{Name}To{Target}UseCase` |
| Sign in / out | `SignIn{Provider}UseCase`, `SignOutUseCase` |
| Extract | `Extract{Name}FromUseCase` |

---

## Checklist Before Submitting Domain Layer Code

- [ ] Domain model has zero framework annotations (`@Serializable`, `@Entity`, etc.)
- [ ] Repository interface uses only Kotlin/Coroutines types — no Supabase types
- [ ] UseCase has both an interface and an `Impl` class
- [ ] UseCase returns `Result<T>` for failable operations
- [ ] UseCase performs at least one business validation before delegating
- [ ] No DataSource is directly referenced in the domain layer
- [ ] `Flow` is used only when reactive observation is genuinely needed
