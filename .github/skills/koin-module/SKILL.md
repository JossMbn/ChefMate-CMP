---
name: koin-module
description: >-
  Patterns for declaring and wiring Koin dependency injection modules in ChefMate.
  Use when asked to "add to koin", "register in DI", "koin module", "dependency injection",
  "inject a dependency", "add the viewmodel to koin", or any DI-related task.
---

# Skill: Koin Module Patterns

ChefMate uses Koin 4.2.1. All DI is declared in `commonMain` — no platform-specific modules
unless a dependency is inherently platform-specific (e.g. the Ktor engine).

---

## Module Structure

```
core/
└── di/
    └── AppModule.kt       ← aggregates all feature modules

feature/
└── {feature}/
    └── {Feature}Module.kt ← declares all dependencies for this feature
```

---

## Feature Module Template

```kotlin
// feature/collection/CollectionModule.kt
val collectionModule = module {

    // ── Data Sources ──────────────────────────────────────────────────────────
    single<CollectionRemoteDataSource> {
        CollectionRemoteDataSourceImpl(supabaseClient = get())
    }
    // Include only if reactive sharing between ViewModels is needed.
    // The CacheEngine is a private val inside the Impl — never registered in Koin:
    // singleOf(::CollectionCacheDataSourceImpl).bind<CollectionCacheDataSource>()

    // ── Repository ────────────────────────────────────────────────────────────
    single<CollectionRepository> {
        CollectionRepositoryImpl(
            remoteDataSource = get()
            // cacheDataSource = get()  ← add if using reactive cache
        )
    }

    // ── Use Cases ─────────────────────────────────────────────────────────────
    single<ObserveCollectionsUseCase> { ObserveCollectionsUseCaseImpl(repository = get()) }
    single<LoadCollectionsUseCase> { LoadCollectionsUseCaseImpl(repository = get()) }
    single<CreateCollectionUseCase> { CreateCollectionUseCaseImpl(repository = get()) }
    single<UpdateCollectionUseCase> { UpdateCollectionUseCaseImpl(repository = get()) }
    single<DeleteCollectionUseCase> { DeleteCollectionUseCaseImpl(repository = get()) }
    single<MoveRecipeToCollectionsUseCase> {
        MoveRecipeToCollectionsUseCaseImpl(repository = get())
    }

    // ── ViewModels ────────────────────────────────────────────────────────────
    viewModelOf(::HomeViewModel)
    viewModelOf(::CollectionDetailViewModel)
}
```

---

## Aggregator Module (AppModule)

```kotlin
// core/di/AppModule.kt
val appModule = module {
    includes(
        networkModule,      // SupabaseClient, Ktor engine
        authModule,
        collectionModule,
        recipeModule,
        // add new feature modules here
    )
}
```

---

## Network Module (core)

```kotlin
// core/di/NetworkModule.kt
val networkModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
            install(Functions)
        }
    }
}
```

---

## Scope Rules

| What | Scope | Reason |
|---|---|---|
| `SupabaseClient` | `single {}` | One shared client for the entire app |
| `*CacheDataSource` / `*CacheDataSourceImpl` | `singleOf(...).bind<Interface>()` | Singleton — owns its private `CacheEngine`, shared across repositories; `CacheEngine` itself is **never** in Koin |
| `*RemoteDataSource` | `single {}` | Stateless — safe to share |
| `*Repository` | `single {}` | Stateless — coordinates datasources |
| `*UseCase` | `single {}` | Stateless — single responsibility |
| `*ViewModel` | `viewModelOf()` | Koin creates/destroys with the composable lifecycle |

Never use `factory {}` unless you have a justified reason to create a new instance every time.
Never use `scoped {}` — ChefMate does not use Koin scopes.

---

## ViewModel Injection in Composables

Always use `koinViewModel()` — only in `{Name}Root` composables.

```kotlin
// ✅ Correct — in Root composable only
@Composable
fun CollectionRoot(
    viewModel: CollectionViewModel = koinViewModel(),
    navigator: CollectionNavigator
) { ... }

// ❌ Wrong — koinViewModel() in Page or Content
@Composable
private fun CollectionPage(...) {
    val viewModel: CollectionViewModel = koinViewModel() // NEVER
}
```

---

## Injecting Multiple UseCases into a ViewModel

```kotlin
class HomeViewModel(
    private val observeCollections: ObserveCollectionsUseCase,
    private val loadCollections: LoadCollectionsUseCase,
    private val deleteCollection: DeleteCollectionUseCase
) : ViewModel()

// Koin wires this automatically with viewModelOf:
viewModelOf(::HomeViewModel)
// Koin resolves ObserveCollectionsUseCase, LoadCollectionsUseCase,
// DeleteCollectionUseCase from the module — all must be declared as single {}
```

---

## Platform-Specific Dependencies

The Ktor HTTP engine is platform-specific. Declare it using `expect/actual` or
directly in the platform-specific source sets:

```kotlin
// androidMain
actual fun httpClientEngine(): HttpClientEngine = OkHttp.create()

// iosMain
actual fun httpClientEngine(): HttpClientEngine = Darwin.create()

// In networkModule (commonMain)
single {
    createSupabaseClient(...) {
        // Ktor engine is picked up automatically via platform source sets
        // or passed as httpEngine = httpClientEngine()
    }
}
```

---

## Koin Initialization (Android)

```kotlin
// androidMain — Application class
class ChefMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChefMateApplication)
            modules(appModule)
        }
    }
}
```

```kotlin
// iosMain — called from Swift AppDelegate or @main
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
```

---

## Adding a New Dependency — Checklist

- [ ] DataSource interface → `single<Interface> { Impl(get()) }`
- [ ] CacheDataSource (if needed) → `singleOf(::CacheDataSourceImpl).bind<CacheDataSource>()` — never register `CacheEngine` in Koin
- [ ] Repository → `single<Repository> { RepositoryImpl(get(), get()) }`
- [ ] Each UseCase → `single<UseCase> { UseCaseImpl(get()) }`
- [ ] ViewModel → `viewModelOf(::ViewModel)`
- [ ] Feature module included in `AppModule.kt`
- [ ] No `factory {}` unless explicitly justified
- [ ] No `koinViewModel()` outside of `Root` composables
