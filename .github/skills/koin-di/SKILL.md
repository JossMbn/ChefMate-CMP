---
name: koin-di
description:  Koin dependency injection conventions used in the ChefMate project, including module organization, scoping rules, and best practices for registering dependencies across all layers (Presentation, Domain, Data, Core).
---

# Koin Dependency Injection Skill

## Overview

This skill defines the Koin dependency injection conventions used in the ChefMate project. Koin is used across all layers — Presentation, Domain, Data, and Core — with a modular setup and constructor injection.

---

## Module Organization

All DI modules live in `di/` and are organized by layer:

```
di/
├── AppModule.kt                     # Core singletons (SupabaseClient)
├── InitKoin.kt                      # Koin initialization entry point
├── presentation/
│   └── ViewModelModule.kt           # ViewModel registrations
├── domain/
│   └── UseCaseModule.kt             # Use case registrations
└── data/
    ├── RepositoryModule.kt          # Repository registrations
    └── DataSourceModule.kt          # Data source registrations
```

---

## Module Definitions

### AppModule — Core singletons

```kotlin
val appModule = module {

    // =============================================================================================
    // Supabase
    // =============================================================================================

    single<SupabaseClient> { SupabaseFactory.createSupabaseClient() }
}
```

### ViewModelModule — ViewModel registrations

Use `viewModelOf(::…)` for all ViewModels. Koin resolves constructor parameters automatically.

```kotlin
val viewModelModule = module {

    // =============================================================================================
    // Home
    // =============================================================================================

    viewModelOf(::HomeViewModel)

    // =============================================================================================
    // Authentication
    // =============================================================================================

    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
}
```

### UseCaseModule — Use case registrations

Use `factoryOf(::…Impl).bind<…UseCase>()` to bind the implementation to its interface. Use cases are **factory-scoped** (new instance per injection).

```kotlin
val useCaseModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    factoryOf(::SignInWithEmailUseCaseImpl).bind<SignInWithEmailUseCase>()
    factoryOf(::SignOutUseCaseImpl).bind<SignOutUseCase>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    factoryOf(::CreateManualRecipeUseCaseImpl).bind<CreateManualRecipeUseCase>()
}
```

**Special cases** — when a use case needs a custom factory function (e.g., `expect`/`actual`):

```kotlin
factory { createValidateAndPrepareRecipeImageUseCase() }.bind<ValidateAndPrepareRecipeImageUseCase>()
```

**Non-interface use cases** — when the use case is a class without a separate interface:

```kotlin
factoryOf(::ObserveAuthenticationStatusUseCase)
```

### RepositoryModule — Repository registrations

Use `singleOf(::…Impl).bind<…Repository>()`. Repositories are **singletons**.

```kotlin
val repositoryModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    singleOf(::RecipeRepositoryImpl).bind<RecipeRepository>()
}
```

### DataSourceModule — Data source registrations

Use `singleOf(::…Impl).bind<…DataSource>()`. Data sources are **singletons**.

```kotlin
val dataSourceModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    singleOf(::AuthenticationRemoteDataSourceImpl).bind<AuthenticationRemoteDataSource>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    singleOf(::RecipeRemoteDataSourceImpl).bind<RecipeRemoteDataSource>()
}
```

---

## Koin Initialization

Koin is initialized via `initKoin()` in `di/InitKoin.kt`, which loads all modules:

```kotlin
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            viewModelModule,
            useCaseModule,
            repositoryModule,
            dataSourceModule
        )
    }
}
```

- The `config` parameter allows platform-specific configuration (e.g., Android `Context`).
- Call `initKoin()` from `androidMain` and `iosMain` entry points.

---

## Scoping Rules

| Component | Koin Scope | DSL | Reason |
|-----------|-----------|-----|--------|
| SupabaseClient | Singleton | `single<…> { }` | One client for the entire app |
| Repository | Singleton | `singleOf(::…Impl).bind<…>()` | Stateless, reuse across features |
| Data Source | Singleton | `singleOf(::…Impl).bind<…>()` | Stateless, reuse across features |
| Use Case | Factory | `factoryOf(::…Impl).bind<…>()` | Lightweight, no shared state |
| ViewModel | ViewModel | `viewModelOf(::…)` | Lifecycle-scoped by Compose |

---

## Consuming Dependencies

### In ViewModels — Constructor injection

```kotlin
class SignInViewModel(
    private val signInWithEmailUseCase: SignInWithEmailUseCase
) : ViewModel() { /* ... */ }
```

### In Composables — `koinViewModel()`

```kotlin
@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: HomeNavigator
) { /* ... */ }
```

### Rules

- **Always** use constructor injection — never call `get()` or `inject()` inside classes.
- **Never** access the Koin container directly outside of module definitions and `initKoin()`.
- `koinViewModel()` is the only Koin call allowed in composables.

---

## Section Comment Blocks

Organize registrations within each module using section comment blocks:

```kotlin
// =============================================================================================
// <Feature Name>
// =============================================================================================
```

Group registrations by feature domain (Authentication, Recipe, Account, etc.).

---

## Registering a New Feature

When adding a new feature, register its components in this order:

1. **DataSourceModule** — `singleOf(::…RemoteDataSourceImpl).bind<…RemoteDataSource>()`
2. **RepositoryModule** — `singleOf(::…RepositoryImpl).bind<…Repository>()`
3. **UseCaseModule** — `factoryOf(::…UseCaseImpl).bind<…UseCase>()`
4. **ViewModelModule** — `viewModelOf(::…ViewModel)`

Add a new section comment block for the feature in each module file.

---

## Common Mistakes to Avoid

| ❌ Don't | ✅ Do |
|----------|------|
| Use `single` for use cases | Use `factoryOf` — use cases are lightweight |
| Use `factory` for repositories/data sources | Use `singleOf` — they are stateless singletons |
| Forget `.bind<Interface>()` | Always bind implementation to its interface |
| Call `get()` inside a ViewModel | Use constructor injection |
| Register in the wrong module | Match the layer: ViewModel → ViewModelModule, etc. |
