# ChefMate — Copilot Instructions

## Project Overview

ChefMate is a cross-platform mobile recipe management application targeting Android and iOS,
built with Kotlin Multiplatform and Compose Multiplatform (shared UI).

Core features:
- Create and manage recipes manually
- Extract recipes from photos, blog URLs, and social media posts via AI (Supabase Edge Function)
- Organise recipes into named Collections
- Add/remove recipes from multiple Collections

## Tech Stack & Exact Versions

| Concern | Library | Version |
|---|---|---|
| Language | Kotlin | 2.3.21 |
| UI | Compose Multiplatform | 1.10.3 |
| UI Components | Material 3 | 1.10.0-alpha05 |
| Build | AGP | 9.0.1 |
| Android minSdk | — | 24 |
| Android compileSdk / targetSdk | — | 36 |
| DI | Koin | 4.2.1 |
| Lifecycle / ViewModel | androidx-lifecycle | 2.10.0 |
| Navigation | Compose Navigation (JetBrains) | 2.9.2 |
| Backend | Supabase-kt BOM | 3.6.0 |
| HTTP engine | Ktor | 3.4.3 |
| Serialization | kotlinx.serialization | 1.11.0 |
| Coroutines | kotlinx.coroutines | 1.10.2 |
| Images | Coil 3 | 3.4.0 |
| Environment config | BuildKonfig | 0.19.0 |
| Splash screen | core-splashscreen | 1.2.0 |

## Project Module Structure

This is **not** a multi-module Gradle project. There are three modules only:
- `composeApp/androidMain` — Android entry point (MainActivity, Application class)
- `composeApp/iosMain` — iOS entry point
- `composeApp/commonMain` — **all shared code**: UI, domain, data, DI

```
composeApp/commonMain/kotlin/com/chefmate/
├── core/
│   ├── di/               # Root Koin module wiring all feature modules
│   ├── navigation/       # Root NavHost, top-level graph
│   ├── theme/            # ChefMateTheme, Color, Type, Shape
│   ├── ui/               # Shared generic components (buttons, loaders, etc.)
│   ├── network/          # SupabaseClient factory, safeExecution extension
│   └── utils/            # Extensions, helpers
└── feature/
    └── {featureName}/    # e.g. auth, home, collection, recipe
        ├── domain/
        │   ├── model/    # Pure Kotlin data classes — NO serialization annotations
        │   ├── repository/ # Repository interfaces only
        │   └── usecase/  # UseCase interfaces + implementations
        ├── data/
        │   ├── dto/      # @Serializable DTOs with @SerialName
        │   ├── mapper/   # Mapper<Domain, Dto> implementations
        │   ├── datasource/
        │   │   ├── remote/   # *RemoteDataSource interface + Impl (Supabase)
        │   │   └── cache/    # *CacheDataSource interface + Impl (reactive cache via CacheEngine)
        │   └── repository/   # *RepositoryImpl
        └── presentation/
            ├── navigation/   # Route, Navigator interface, NavGraphBuilder extension
            ├── screen/       # *Root, *Page, *Content composables + *ViewModel
            └── preview/      # Preview-only composables (*Preview.kt files)
```

## Architecture — Clean Architecture + MVI + UDF

### Mandatory layer rules

- **Domain layer** has zero dependencies on Android, Compose, Supabase, or Ktor.
- **Data layer** implements domain interfaces. DTOs never leak into domain or presentation.
- **Presentation layer** depends only on domain (UseCase interfaces and Domain models).
- Dependency direction is always: `presentation → domain ← data`

### MVI pattern — non-negotiable

Every screen has exactly:
- `{Name}State` — `data class`, represents the complete UI state
- `{Name}Action` — `sealed interface`, user intents sent to the ViewModel
- `{Name}Event` — `sealed interface`, one-shot events (navigation, snackbar, etc.) — only if needed
- `{Name}ViewModel` — exposes `state: StateFlow<{Name}State>` and optionally `event: SharedFlow<{Name}Event>`
- Single public function `onAction(action: {Name}Action)` — the only entry point from the UI

### ViewModel template

```kotlin
class ExampleViewModel(
    private val exampleUseCase: ExampleUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<ExampleEvent>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(ExampleState())
    val state = _state
        .onStart { loadInitialData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ExampleState()
        )

    fun onAction(action: ExampleAction) {
        when (action) {
            is ExampleAction.OnItemClick -> handleItemClick(action.id)
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            exampleUseCase()
                .onSuccess { data -> _state.update { it.copy(isLoading = false, data = data) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, error = error) } }
        }
    }
}
```

## Compose Screen Hierarchy

Every screen follows this **strict four-level hierarchy**:

### Level 1 — `{Name}Root` (internal to navigation)
- Sole location for `koinViewModel()` and `collectAsStateWithLifecycle()`
- Sole location for one-shot event observation via `LaunchedEffect(Unit)`
- Calls `{Name}Page`, passing state, onAction, and navigator

### Level 2 — `{Name}Page` (private)
- Contains only the `Scaffold`
- Passes `innerPadding` to `{Name}Content` via `Modifier.padding(innerPadding)`
- Contains the `Preview` annotated composable

### Level 3 — `{Name}Content` (private)
- The body of the page
- Calls individual Components
- Handles `when(state.uiState)` branching (Loading / Success / Error / Empty)

### Level 4 — Components (private or shared in `core/ui/`)
- Stateless, reusable UI pieces
- Always accept `Modifier` as the first parameter with `Modifier` as default value

### Example structure

```kotlin
@Composable
fun ExampleRoot(
    viewModel: ExampleViewModel = koinViewModel(),
    navigator: ExampleNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // One-shot events
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ExampleEvent.ShowError -> { /* show snackbar */ }
            }
        }
    }

    ExamplePage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun ExamplePage(
    state: ExampleState,
    onAction: (ExampleAction) -> Unit,
    navigator: ExampleNavigator
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ExampleContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun ExampleContent(
    modifier: Modifier = Modifier,
    state: ExampleState,
    onAction: (ExampleAction) -> Unit,
    navigator: ExampleNavigator
) {
    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorState(message = state.error.message)
        state.items.isEmpty() -> EmptyState()
        else -> { /* content */ }
    }
}
```

## Navigation Pattern

Each feature has a `navigation/` package with one file containing:
1. `@Serializable` route object/class
2. `{Name}Navigator` interface with navigation functions
3. `{Name}NavigatorImpl` implementing the interface with a `NavController`
4. `NavGraphBuilder.{featureName}Page(controller: NavController)` extension

```kotlin
@Serializable
data object ExampleRoute

@Stable
interface ExampleNavigator {
    fun navigateBack()
    fun navigateToDetail(id: String)
}

class ExampleNavigatorImpl(
    private val controller: NavController? = null
) : ExampleNavigator {
    override fun navigateBack() { controller?.navigateUp() }
    override fun navigateToDetail(id: String) {
        controller?.navigate(ExampleDetailRoute(id = id))
    }
}

fun NavGraphBuilder.examplePage(controller: NavController) {
    composable<ExampleRoute> {
        ExampleRoot(navigator = ExampleNavigatorImpl(controller))
    }
}
```

## State Collection — CRITICAL RULE

**Always use `collectAsStateWithLifecycle()`.**
**Never use `collectAsState()`.**

`collectAsStateWithLifecycle` is Lifecycle-aware and stops collection when the app goes to
background, preventing unnecessary work and potential leaks. It requires the
`androidx-lifecycle-runtime-compose` dependency which is already in `libs.versions.toml`.

## Coroutines & Flow Rules

- Use `suspend fun` by default in DataSources, Repositories, and UseCases.
- Use `Flow` in the Repository/DataSource layer **only** when multiple ViewModels need to
  observe shared mutable state (e.g. collections count must update on Home when modified
  in RecipeDetail). In that case, use a dedicated `{Name}CacheDataSource` backed by a
  `CacheEngine<T>` (from `core/data/cache/`) as the reactive cache. The `CacheEngine` is
  always a `private val` inside the `Impl` — never injectable. See data-layer instructions
  for the full pattern.
- `viewModelScope.launch {}` for fire-and-forget mutations.
- `viewModelScope.launch {}` + `Flow.collect {}` for subscribing to reactive repositories.
- Never use `GlobalScope`.
- Always re-throw `CancellationException` — never catch it silently.

## Supabase Usage

- Always use the BOM: `implementation(platform("io.github.jan-tennert.supabase:bom:3.6.0"))`
- Active modules: `postgrest-kt`, `auth-kt`, `storage-kt`, `functions-kt`
- All Supabase calls **must** go through `safeExecution {}` — never write bare try/catch
  in a DataSource.
- Prefer `postgrest.rpc()` for complex queries. Direct table access via `postgrest.from()`
  is acceptable for simple CRUD where no RPC function exists.
- Ktor engine: `ktor-client-okhttp` on Android, `ktor-client-darwin` on iOS.
- Never use `supabase-realtime` — it is not part of this project.

## Error Handling

- All failable operations return `Result<T>`.
- `Result` is propagated up through all layers to the ViewModel.
- The ViewModel is the first layer that handles errors by updating `{Name}State`.
- Never swallow a `Result.failure` silently in a DataSource or Repository without logging.
- Use the existing `safeExecution` + `RestException.toError()` pattern in `core/network/`.

## BuildKonfig — Environment Config

- All API URLs, Supabase keys, and environment flags come from `BuildKonfig`.
- Never hardcode `https://...supabase.co` or any API key anywhere in the source code.
- Access config via `BuildConfig.SUPABASE_URL`, `BuildConfig.SUPABASE_ANON_KEY`, etc.

## Recipe Extraction — AI Integration Rule

**Never call an AI API (OpenAI, Gemini, Anthropic, etc.) directly from the client.**
The extraction pipeline is:
1. Upload image/URL to Supabase Storage or pass URL to Edge Function
2. Call Supabase Edge Function via `functions-kt`
3. Edge Function handles the AI call server-side and returns a structured recipe JSON
4. Client parses the JSON into a `RecipeDomain` via a Mapper

## Dependency Injection — Koin

- All modules declared in `commonMain`.
- Per-feature module file: `{feature}Module.kt` in `feature/{name}/`
- Root DI file in `core/di/AppModule.kt` which aggregates all feature modules.
- `single {}` for DataSources, Repositories, and UseCases.
- `viewModelOf(::MyViewModel)` for ViewModels.
- No `factory {}` unless the object must not be shared.

## Naming Conventions

| Artefact | Convention | Example |
|---|---|---|
| Feature package | lowercase | `feature/collection/` |
| Domain model | `{Name}Domain` | `CollectionDomain` |
| DTO | `{Name}Dto` | `CollectionDto` |
| Mapper | `{Name}Mapper` | `CollectionMapper` |
| Repository interface | `{Name}Repository` | `CollectionRepository` |
| Repository impl | `{Name}RepositoryImpl` | `CollectionRepositoryImpl` |
| Remote DataSource interface | `{Name}RemoteDataSource` | `CollectionRemoteDataSource` |
| Remote DataSource impl | `{Name}RemoteDataSourceImpl` | `CollectionRemoteDataSourceImpl` |
| Cache DataSource interface | `{Name}CacheDataSource` | `CollectionCacheDataSource` |
| Cache DataSource impl | `{Name}CacheDataSourceImpl` | `CollectionCacheDataSourceImpl` |
| UseCase interface | `{Verb}{Name}UseCase` | `CreateCollectionUseCase` |
| UseCase impl | `{Verb}{Name}UseCaseImpl` | `CreateCollectionUseCaseImpl` |
| ViewModel | `{Name}ViewModel` | `CollectionViewModel` |
| State | `{Name}State` | `CollectionState` |
| Action | `{Name}Action` | `CollectionAction` |
| Event | `{Name}Event` | `CollectionEvent` |
| Route | `{Name}Route` | `CollectionRoute` |
| Navigator interface | `{Name}Navigator` | `CollectionNavigator` |
| Navigator impl | `{Name}NavigatorImpl` | `CollectionNavigatorImpl` |
| Root composable | `{Name}Root` | `CollectionRoot` |
| Page composable | `{Name}Page` | `CollectionPage` |
| Content composable | `{Name}Content` | `CollectionContent` |
| Preview file | `{Name}Preview.kt` | `CollectionPreview.kt` |

## Forbidden Patterns

- `collectAsState()` — use `collectAsStateWithLifecycle()` instead
- Business logic inside a `@Composable` function
- Hardcoded colors (`Color(0xFF...)`) — use `MaterialTheme.colorScheme`
- Hardcoded strings in UI — use string resources via `stringResource()`
- Hardcoded API keys or URLs — use BuildKonfig
- Direct AI API calls from client code — use Supabase Edge Functions
- `GlobalScope` — use `viewModelScope` or a provided `CoroutineScope`
- Catching `CancellationException` without rethrowing
- DTOs in domain or presentation layer
- Domain models annotated with `@Serializable`
- Skipping the `safeExecution {}` wrapper for Supabase calls
- `Room` or `SQLDelight` without an explicit architectural decision
- `by lazy` at the top level of a Composable
- Mutable state (`var`) in a Composable that should be in the ViewModel
