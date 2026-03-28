---
name: kotlin-convention
description: Guide for writing idiomatic, clean, and maintainable Kotlin code. Use when reviewing Kotlin code style, applying language best practices.
---

# Kotlin Convention Skill

## Overview

This skill defines the Kotlin coding conventions and idioms used throughout the ChefMate project. The project follows `kotlin.code.style=official` and targets Kotlin 2.3.20 with Kotlin Multiplatform.

---

## Sealed Types

- Prefer `sealed interface` over `sealed class` for Action, Event, and DialogState hierarchies.
- **Exception**: Error types (`NetworkError`, `AuthenticationError`) use `sealed class` extending `Throwable()` because they need to be throwable and caught in `Result<T>` chains.
- Use `data object` for parameterless subtypes and `data class` for subtypes with properties.

```kotlin
// ✅ sealed interface — for Action, Event, DialogState
sealed interface HomeAction {
    data class OnTitleChange(val newTitle: String) : HomeAction
    data object OnSaveClick : HomeAction
}

// ✅ sealed class — for error types that extend Throwable
sealed class NetworkError : Throwable() {
    class NetworkConnectionError : NetworkError()
    class TimeoutError : NetworkError()
    data class BadRequest(override val message: String?) : NetworkError()
}
```

---

## Data Classes

- Use `data class` for State, domain models, and UI data models.
- Provide sensible defaults so instances can be created with `ClassName()`.
- Use `copy()` for immutable state updates.
- Use `@Stable` annotation (from `androidx.compose.runtime`) on State data classes that contain complex nested types (e.g., `ImmutableList`, custom data classes) to help Compose skip unnecessary recomposition.

```kotlin
data class HomeState(
    val contentView: HomeContentView = HomeContentView.Loading,
    val isLoading: Boolean = false
)

// @Stable for complex nested state
@Stable
data class ManualRecipeCreationState(
    val isCreatingRecipe: Boolean = false,
    val recipe: RecipeUiData = RecipeUiData(),
    val dialogState: ManualRecipeCreationDialogState? = null
)

// State mutation
_state.update { it.copy(isLoading = true) }
```

---

## Enums

- Use `enum class` for finite sets of values (e.g., `ContentView`, `RecipeDifficulty`, `TemperatureUnit`).
- Place enum definitions in the same file as the State they belong to, or in their own file under `model/`.

```kotlin
enum class HomeContentView {
    Loading, Content
}
```

---

## Named Arguments

- Use named arguments for function calls with **two or more parameters**.
- Always use named arguments for Koin DSL, Supabase queries, and navigation calls.

```kotlin
// ✅ Named arguments
recipeRepository.createRecipe(
    recipe = recipe,
    collectionIds = emptyList()
)

// ✅ Single parameter — named argument optional
_state.update { it.copy(isLoading = true) }
```

---

## Extension Functions

- Use extension functions for cross-cutting transformations (e.g., `Throwable.toUiText()`, `RecipeUiData.toDomain()`).
- Place in dedicated extension files (e.g., `ExceptionExtension.kt`, `ModifierExtensions.kt`).
- Prefer extension functions over utility classes.

```kotlin
// core/presentation/extension/ExceptionExtension.kt
fun Throwable.toUiText(): UiText {
    return when (this) {
        is NetworkError.NetworkConnectionError -> UiText.ResourceString(Res.string.error_connection)
        else -> UiText.ResourceString(Res.string.error_request_failed)
    }
}
```

---

## Result<T> Error Handling

- Use Kotlin `Result<T>` for all data and domain operations.
- Use `runCatching { }` in data sources to produce `Result<T>`.
- Chain with `.onSuccess { }`, `.onFailure { }`, and `.also { }`.
- Never use `try/catch` in ViewModels — always rely on `Result<T>`.

```kotlin
// Data source
suspend fun getItem(id: String): Result<ItemDomain> = runCatching {
    // Supabase call
}

// ViewModel
someUseCase(params)
    .onSuccess { item -> /* handle success */ }
    .onFailure { error -> SnackbarController.sendError(error = error) }
    .also { _state.update { it.copy(isLoading = false) } }
```

---

## Operator Overloading

- Use `operator fun invoke()` for use cases to allow calling them as functions.

```kotlin
interface CreateManualRecipeUseCase {
    suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain>
}

// Usage in ViewModel
createManualRecipeUseCase(recipe = mappedRecipe)
```

---

## Interface + Impl Pattern

- Define an **interface** for use cases, repositories, and data sources.
- Provide an **Impl class** in the same file (use cases) or in the data layer (repositories, data sources).
- Bind via Koin: `.bind<Interface>()`.

```kotlin
// Same file — use case
interface SignOutUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class SignOutUseCaseImpl(
    private val authRepository: AuthenticationRepository
) : SignOutUseCase {
    override suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
}
```

---

## Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Package | lowercase, dot-separated | `com.jmabilon.chefmate.feature.home` |
| Class / Interface | PascalCase | `HomeViewModel`, `RecipeRepository` |
| Implementation | Suffixed with `Impl` | `RecipeRepositoryImpl` |
| Domain model | Suffixed with `Domain` | `RecipeDomain`, `CollectionDomain` |
| UI data model | Suffixed with `UiData` | `RecipeUiData`, `RecipeInfoUiData` |
| State | Suffixed with `State` | `HomeState` |
| Action | Suffixed with `Action` | `HomeAction` |
| Event | Suffixed with `Event` | `HomeEvent` |
| Route | Suffixed with `Route` | `HomeRoute` |
| Navigator | Suffixed with `Navigator` / `NavigatorImpl` | `HomeNavigator`, `HomeNavigatorImpl` |
| Extension file | `<Subject>Extension.kt` | `ExceptionExtension.kt` |
| Composable file | `<Name>Page.kt` | `HomePage.kt` |
| Private backing flow | Prefixed with `_` | `_state`, `_event` |
| Action subtypes | Prefixed with `On` | `OnTitleChange`, `OnSaveClick` |
| DS components | Prefixed with `CM` | `CMTopAppBar`, `CMTextField` |

---

## Section Comment Blocks

Organize code sections with `=`-separator comment blocks:

```kotlin
// =============================================================================================
//  Section Name
// =============================================================================================
```

Use in ViewModels (Private Properties, Public Properties, Public Methods, Private Methods), DI modules (by feature), repository implementations (by entity), and navigation files (Route, Navigator, Graph extension).

---

## Empty Blocks

- Use `/* no-op */` for intentionally empty lambdas and blocks.

```kotlin
onAction = { /* no-op */ }
defaultConfigs { /* no-op */ }
```

---

## File Organization

- **One major class/composable per file** — keep files focused.
- Helper data classes (e.g., `RecipeUiData`, `RecipeInfoUiData`) may share a file with the State if they are tightly coupled.
- Place `@file:OptIn(...)` annotations at the top of the file when needed.

---

## Kotlin Multiplatform

- Use `expect`/`actual` declarations for platform-specific implementations.
- `expect` declarations go in `commonMain`, `actual` implementations in `androidMain` and `iosMain`.
- Minimize platform-specific code — keep as much as possible in `commonMain`.

```kotlin
// commonMain
expect fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase

// androidMain
actual fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase {
    return ValidateAndPrepareRecipeImageUseCaseAndroid()
}

// iosMain
actual fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase {
    return ValidateAndPrepareRecipeImageUseCaseIos()
}
```

---

## Immutable Collections

- Use `ImmutableList` from `kotlinx.collections.immutable` for list properties in state and UI models.
- Use `persistentListOf()` for default empty lists.
- Use `.toImmutableList()` when converting from mutable lists.

```kotlin
data class RecipeUiData(
    val ingredients: ImmutableList<RecipeIngredientUiData> = persistentListOf()
)
```

---

## Nullability

- Prefer non-null types with defaults over nullable types.
- Use nullable types only when `null` has distinct semantic meaning (e.g., `dialogState: DialogState? = null` where `null` means "no dialog").
- Use safe calls (`?.`) and elvis operator (`?:`) — avoid `!!`.
