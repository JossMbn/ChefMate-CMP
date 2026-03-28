---
name: clean-architecture
description: Clean Architecture layer rules, dependency direction, and patterns for the Courses U Android project. Use when structuring new features, reviewing layer boundaries, or deciding where code belongs.
---

# Clean Architecture Skill

## Overview

This skill defines the Clean Architecture patterns used in the ChefMate project. The architecture enforces strict **layer separation** with unidirectional dependency flow: **Presentation → Domain ← Data**. The Domain layer has no dependencies on Presentation or Data.

---

## Layer Dependency Rules

```
┌─────────────────┐
│  Presentation    │  feature/, designsystem/
│  (feature layer) │
└────────┬────────┘
         │ depends on
         ▼
┌─────────────────┐
│     Domain       │  domain/
│  (business logic)│
└────────▲────────┘
         │ depends on
┌────────┴────────┐
│      Data        │  data/
│  (implementation)│
└─────────────────┘

Both Presentation and Data may depend on Core (core/).
```

### Strict rules

- **Domain** depends only on Kotlin stdlib and `core/` utilities (Mapper, NetworkError).
- **Data** implements Domain interfaces — depends on Domain + Core.
- **Presentation** depends on Domain (use cases, models) + Core + DesignSystem.
- **Never** import `data/` from `feature/` or `domain/`.
- **Never** import `feature/` from `data/` or `domain/`.

---

## Domain Layer (`domain/`)

### Structure

```
domain/<feature>/
├── model/                   # Domain models (suffixed with Domain)
│   ├── <Name>Domain.kt
│   └── error/               # Domain-specific error types (sealed class : Throwable)
│       └── <Name>Error.kt
├── repository/              # Repository interfaces
│   └── <Name>Repository.kt
├── usecase/                 # Use case interface + Impl
│   └── <Name>UseCase.kt
└── mapper/                  # Domain ↔ UI mappers (extension functions or Mapper interface)
    └── <Name>Mapper.kt
```

### Domain Models

- Suffix with `Domain` (e.g., `RecipeDomain`, `CollectionDomain`).
- Use Kotlin `data class` — no framework dependencies.
- Domain models represent the **business entity**, not the API response or UI representation.

```kotlin
data class RecipeDomain(
    val id: String,
    val title: String,
    val ingredients: List<RecipeIngredientDomain>,
    val instructions: List<RecipeInstructionDomain>
)
```

### Domain Error Types

- Use `sealed class` extending `Throwable()` for domain-specific errors (not `sealed interface`, since they must be throwable).
- Error types live in `domain/<feature>/model/error/`.
- Network errors live in `core/network/model/error/` and also extend `Throwable()`.

```kotlin
sealed class AuthenticationError : Throwable() {
    class InvalidCredentials : AuthenticationError()
    class EmailNotConfirmed : AuthenticationError()
    class UserAlreadyExists : AuthenticationError()
}
```

### Repository Interfaces

- Define the contract — **no implementation details**.
- All methods are `suspend` and return `Result<T>`.
- Organize methods with section comment blocks.

```kotlin
interface RecipeRepository {

    // =============================================================================================
    // Recipe
    // =============================================================================================

    suspend fun getRecipeById(recipeId: String): Result<RecipeDomain>
    suspend fun createRecipe(recipe: RecipeDomain, collectionIds: List<String>): Result<RecipeDomain>
    suspend fun deleteRecipe(recipeId: String): Result<Unit>
}
```

### Use Cases

- Each use case has an **interface** and an **Impl class** in the same file.
- Use `suspend operator fun invoke(…): Result<T>` for the single entry point.
- The Impl class receives the repository via constructor injection.
- Use cases encapsulate **one business operation** — keep them focused.

```kotlin
interface CreateManualRecipeUseCase {
    suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain>
}

class CreateManualRecipeUseCaseImpl(
    private val recipeRepository: RecipeRepository
) : CreateManualRecipeUseCase {

    override suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain> {
        return recipeRepository.createRecipe(
            recipe = recipe,
            collectionIds = emptyList()
        )
    }
}
```

### Mappers

- Use the `Mapper<OutputType, InputType>` interface from `core/domain/` for class-based mappers.
- Use extension functions (e.g., `fun RecipeUiData.toDomain(): RecipeDomain`) for simple one-off conversions.
- Mappers live in `domain/<feature>/mapper/`.

---

## Data Layer (`data/`)

### Structure

```
data/<feature>/
├── <Name>RepositoryImpl.kt           # Implements domain repository interface
└── source/
    └── remote/
        ├── <Name>RemoteDataSource.kt      # Interface
        ├── <Name>RemoteDataSourceImpl.kt  # Implementation (Supabase calls)
        ├── dto/                           # Data Transfer Objects (API response models)
        ├── model/                         # Data-layer-specific models
        ├── parameter/                     # Query parameter models
        └── request/                       # Request body models
```

### Repository Implementation

- Implements the domain `Repository` interface.
- Delegates all work to the data source — the repository is a **thin coordination layer**.
- Constructor-injected with the data source.

```kotlin
class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    override suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> {
        return recipeRemoteDataSource.getRecipeById(recipeId = recipeId)
    }
}
```

### Remote Data Source

- Interface + Impl pattern (same as use cases).
- Impl receives `SupabaseClient` via constructor injection.
- All methods return `Result<T>` — wrap Supabase calls in `runCatching { }`.
- Use named arguments for all Supabase/Ktor calls.

---

## Core Layer (`core/`)

### Structure

```
core/
├── data/extension/          # Data-layer extensions (e.g., PostgrestResult parsing)
├── domain/                  # Mapper interface
├── network/model/error/     # NetworkError sealed hierarchy
├── presentation/            # ObserveAsEvent, SnackbarController, extensions
└── supabase/                # SupabaseFactory, extension functions
```

### Key Utilities

| Utility | Location | Purpose |
|---------|----------|---------|
| `Mapper<O, I>` | `core/domain/Mapper.kt` | Generic mapping interface with `convert()` and `convertOrEmpty()` |
| `NetworkError` | `core/network/model/error/` | Sealed hierarchy for network failures |
| `SnackbarController` | `core/presentation/` | Global error snackbar channel |
| `ObserveAsEvent` | `core/presentation/` | Lifecycle-aware one-shot event collector |
| `toUiText()` | `core/presentation/extension/` | Maps `Throwable` to `UiText` for display |

---

## Error Handling Flow

```
DataSource (runCatching) → Result<T>
    → Repository (pass-through) → Result<T>
        → UseCase (optional business logic) → Result<T>
            → ViewModel (.onSuccess / .onFailure)
                → SnackbarController.sendError(error)
                    → Throwable.toUiText() → UiText
                        → SnackbarHost displays message
```

---

## Adding a New Feature Across Layers

1. **Domain**: Create `domain/<feature>/model/`, `repository/`, `usecase/`, and optionally `mapper/`.
2. **Data**: Create `data/<feature>/<Name>RepositoryImpl.kt` and `source/remote/` data source interface + impl.
3. **Presentation**: Create `feature/<feature>/` with Page, ViewModel, model/, navigation/.
4. **DI**: Register in `UseCaseModule`, `RepositoryModule`, `DataSourceModule`, and `ViewModelModule`.
5. **Navigation**: Wire in `MainNavHost` or `AuthenticationNavHost`.

---

## Anti-Patterns to Avoid

| ❌ Don't | ✅ Do |
|----------|------|
| Import `data/` classes in `domain/` | Define interfaces in `domain/`, implement in `data/` |
| Import `feature/` classes in `data/` | Data layer is unaware of presentation |
| Put business logic in ViewModel | Extract into a use case |
| Put UI formatting in domain models | Use mappers or UI-specific models (`UiData` suffix) |
| Return raw API responses to ViewModel | Map to domain models in data source |
| Use `try/catch` in ViewModel | Use `Result<T>` from use cases with `.onSuccess`/`.onFailure` |
