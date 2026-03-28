# Copilot Instructions for ChefMate

## Project Overview

ChefMate is a **Kotlin Multiplatform (KMP)** recipe management application targeting **Android** and **iOS**, built with **Compose Multiplatform** for shared UI. The backend is powered by **Supabase** (Auth, Postgrest, Storage, Functions).

- **Package**: `com.jmabilon.chefmate`
- **Kotlin**: 2.3.20
- **Compose Multiplatform**: 1.10.3
- **Min Android SDK**: 24 · **Target/Compile SDK**: 36

---

## Project Structure

```
composeApp/src/
├── commonMain/kotlin/com/jmabilon/chefmate/
│   ├── core/            # Cross-cutting concerns (network, supabase, mappers, presentation utils)
│   ├── data/            # Repository implementations & data sources (remote) (data layer)
│   ├── designsystem/    # Theme, reusable UI components, sheets, extensions, utils
│   ├── di/              # Koin dependency injection modules
│   ├── domain/          # Domain models, repository interfaces, use cases, mappers (domain layer)
│   └── feature/         # Feature screens (presentation layer)
├── androidMain/         # Android-specific implementations (expect/actual)
└── iosMain/             # iOS-specific implementations (expect/actual)
```

---

## Architecture — Clean Architecture + MVI

This project follows **Clean Architecture** with an **MVI (Model-View-Intent)** presentation pattern.

### Layers

| Layer | Location | Responsibility |
|-------|----------|----------------|
| **Presentation** | `feature/` | Screens, ViewModels, UI state/actions/events, navigation |
| **Domain** | `domain/` | Use cases, repository interfaces, domain models, mappers |
| **Data** | `data/` | Repository implementations, remote data sources |
| **Core** | `core/` | Shared utilities — Supabase client, network error models, base mapper interface, presentation helpers |
| **Design System** | `designsystem/` | Theme (colors, typography, shapes), reusable components (`CM`-prefixed), sheets, extensions |

### MVI Pattern (per feature)

Each feature follows this structure:

```
feature/<name>/
├── <Name>Page.kt              # Composable UI (Root + Page + PageContent + Preview)
├── <Name>ViewModel.kt         # ViewModel with State, Action, Event
├── model/
│   ├── <Name>State.kt         # UI state data class (with ContentView enum: Loading, Content)
│   ├── <Name>Action.kt        # Sealed interface for user actions
│   └── <Name>Event.kt         # Sealed interface for one-shot events
└── navigation/
    └── <Name>Navigation.kt    # Route, Navigator interface, NavigatorImpl, NavGraphBuilder extension
```

---

## Key Conventions

### Kotlin & Compose

- Use **Kotlin** idioms: data classes, sealed interfaces, extension functions, `Result<T>` for error handling.
- Prefer `sealed interface` over `sealed class` for Action/Event types.
- Use `data class` for State; include a `ContentView` enum (`Loading`, `Content`, etc.) to manage screen state.
- Write **Compose Multiplatform** compatible code in `commonMain`. Use `expect`/`actual` for platform-specific implementations in `androidMain`/`iosMain`.
- Name composable files as `<Name>Page.kt`. Each page file should contain:
  - `<Name>Root` — public composable that wires ViewModel + Navigator.
  - `<Name>Page` — private composable receiving state, `onAction` lambda, and navigator.
  - `<Name>PageContent` — private composable for the inner content.
  - A `@Preview` composable at the bottom using `ChefMateTheme` and the default state.
- Use `collectAsStateWithLifecycle()` to observe state in composables.
- Use `koinViewModel()` to obtain ViewModels in Root composables.
- Use `MaterialTheme.colorScheme` for theming — never hardcode colors.
- Prefix reusable design system components with `CM` (e.g., `CMTopAppBar`).

### ViewModel

- Extend `androidx.lifecycle.ViewModel`.
- Expose state via `MutableStateFlow` + `.onStart { }` + `.stateIn(scope, SharingStarted.Lazily, initialValue)`. For complex state composed from multiple flows, use `combine(...)` instead of `.onStart {}`.
- Expose one-shot events via `MutableSharedFlow` + `.asSharedFlow()` (simple broadcast) or `Channel` + `.receiveAsFlow()` (guaranteed delivery).
- Accept user interactions through a single `onAction(action: <Name>Action)` function using a `when` expression.
- Use `@Stable` annotation on State data classes when they contain complex nested types to help Compose skip recomposition.
- Use `ImmutableList` from `kotlinx.collections.immutable` for list properties in state/UI models to optimize Compose recomposition.

### Navigation

- Use **Jetpack Navigation Compose** (multiplatform variant `org.jetbrains.androidx.navigation`).
- Define routes as `@Serializable data object <Name>Route` (or `data class` for routes with arguments).
- Define a `@Stable` `<Name>Navigator` interface with navigation methods.
- Provide a `<Name>NavigatorImpl(controller: NavController? = null)` class implementing the interface.
- Register screens via `NavGraphBuilder` extension functions (e.g., `fun NavGraphBuilder.homePage(controller: NavController)`).

### Dependency Injection (Koin)

- All DI modules live in `di/`:
  - `AppModule.kt` — Supabase client singleton.
  - `di/presentation/ViewModelModule.kt` — `viewModelOf(::…)` registrations.
  - `di/domain/UseCaseModule.kt` — `factoryOf(::…Impl).bind<…UseCase>()` registrations.
  - `di/data/RepositoryModule.kt` — `singleOf(::…Impl).bind<…Repository>()` registrations.
  - `di/data/DataSourceModule.kt` — `singleOf(::…Impl).bind<…DataSource>()` registrations.
- Use **constructor injection** everywhere. Koin resolves dependencies automatically.
- When creating a new feature, register its ViewModel, use cases, repository, and data source in the corresponding DI modules.

### Domain Layer

- Repository interfaces live in `domain/<feature>/repository/`.
- Use cases follow the pattern:
  - An **interface** with `suspend operator fun invoke(…): Result<T>`.
  - An **Impl class** implementing the interface, injected with the repository.
- Domain models are suffixed with `Domain` (e.g., `RecipeDomain`, `CollectionDomain`).
- The `Mapper<OutputType, InputType>` interface in `core/domain/` should be used for data transformations between layers.
- Domain layer should depend only on core utilities (e.g., mappers, network error models) and not on any data or presentation layer code.

### Data Layer

- Repository implementations live in `data/<feature>/` and implement the domain interface.
- Remote data sources live in `data/<feature>/source/remote/` and follow an interface + Impl pattern.
- Use `Result<T>` for all data operations to propagate success/failure.
- Supabase is accessed via the injected `SupabaseClient`; use extension functions from `core/supabase/extension/`.
- Data layer can depend on core utilities (e.g., Supabase client, mappers) but should not depend on presentation layer code.

### Error Handling

- Use Kotlin `Result<T>` throughout the data and domain layers.
- Network errors are modeled in `core/network/model/error/NetworkError.kt`.
- UI text is abstracted via `designsystem/utils/UiText.kt`.

### Build Configuration

- Sensitive keys (Supabase URL/API Key) are stored in `secure.properties` (git-ignored) and injected via **BuildKonfig** plugin.
- Two flavors: `dev` and `prod` — configured in `composeApp/build.gradle.kts`.
- Default flavor is `dev` (set in `gradle.properties`: `buildkonfig.flavor=dev`).
- Use Gradle version catalogs (`gradle/libs.versions.toml`) for all dependency management.

### Code Style

- Follow `kotlin.code.style=official`.
- Use section comment blocks with `=` separators to organize code sections:
  ```kotlin
  // =============================================================================================
  //  Section Name
  // =============================================================================================
  ```
- Keep files focused: one major class/composable per file.
- Named arguments for function calls with multiple parameters.
- Use `/* no-op */` for intentionally empty lambdas/blocks.

---

## When Adding a New Feature

1. Create the feature package under `feature/<name>/`.
2. Add `<Name>Page.kt` with Root, Page, PageContent, and Preview composables.
3. Add `<Name>ViewModel.kt` with State/Action/Event in `model/`.
4. Add `<Name>Navigation.kt` in `navigation/` with Route, Navigator, NavigatorImpl, and NavGraphBuilder extension.
5. If the feature needs data, add domain models, repository interface, use case interface + impl, data source interface + impl, and repository impl.
6. Register all new classes in the appropriate Koin DI modules.
7. Wire the navigation in the parent NavHost (`MainNavHost` or `AuthenticationNavHost`).

## References

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Coroutine Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [KDoc Documentation](https://kotlinlang.org/docs/kotlin-doc.html)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Supabase Kotlin SDK](https://supabase.com/docs/reference/kotlin/introduction)
- [Koin Documentation](https://insert-koin.io/docs/reference/introduction)
