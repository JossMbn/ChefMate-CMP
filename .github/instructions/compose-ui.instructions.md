---
applyTo: "**/*Root.kt,**/*Page.kt,**/*Content.kt,**/*Component*.kt"
---

# Compose UI Instructions

## Screen Hierarchy — Strict Rules

Every screen **must** follow the four-level hierarchy: `Root → Page → Content → Component`.
Never collapse or skip levels. Never add a fifth level.

### `{Name}Root` — Lifecycle & State Owner

- **Visibility**: `internal` or `public` (called from navigation graph)
- **Responsibilities**:
  - Only place where `koinViewModel()` is called
  - Only place where `collectAsStateWithLifecycle()` is called
  - Only place where one-shot events are observed (`LaunchedEffect`)
  - Passes everything down to `{Name}Page` — contains no layout code
- **Never**: layout, Scaffold, business logic, direct Supabase/UseCase calls

```kotlin
@Composable
fun ExampleRoot(
    viewModel: ExampleViewModel = koinViewModel(),
    navigator: ExampleNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ExampleEvent.NavigateBack -> navigator.navigateBack()
            }
        }
    }

    ExamplePage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}
```

### `{Name}Page` — Scaffold Owner

- **Visibility**: `private`
- **Responsibilities**:
  - Contains the `Scaffold` and nothing else
  - Sets up `topBar`, `bottomBar`, `floatingActionButton` if needed
  - Passes `Modifier.padding(innerPadding)` to `{Name}Content`
  - Contains the `@PreviewLightDark` annotated composable (see preview instructions)
- **Never**: business logic, direct ViewModel interaction, complex layout

```kotlin
@Composable
private fun ExamplePage(
    state: ExampleState,
    onAction: (ExampleAction) -> Unit,
    navigator: ExampleNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExampleTopBar(
                title = state.title,
                onBackClick = { onAction(ExampleAction.OnBackClick) }
            )
        }
    ) { innerPadding ->
        ExampleContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction
        )
    }
}
```

### `{Name}Content` — Page Body

- **Visibility**: `private`
- **Responsibilities**:
  - Renders the body of the page
  - Handles the UI state branching (Loading / Success / Error / Empty)
  - Calls individual Components
- **Always** accepts `modifier: Modifier = Modifier` as first parameter
- **Never**: contains a Scaffold, calls ViewModel or UseCase

```kotlin
@Composable
private fun ExampleContent(
    modifier: Modifier = Modifier,
    state: ExampleState,
    onAction: (ExampleAction) -> Unit
) {
    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            ErrorStateComponent(
                modifier = modifier,
                message = state.error,
                onRetry = { onAction(ExampleAction.OnRetryClick) }
            )
        }
        state.items.isEmpty() -> {
            EmptyStateComponent(modifier = modifier)
        }
        else -> {
            LazyColumn(modifier = modifier) {
                items(state.items) { item ->
                    ExampleItemComponent(
                        item = item,
                        onClick = { onAction(ExampleAction.OnItemClick(item.id)) }
                    )
                }
            }
        }
    }
}
```

### Components — Atomic UI Pieces

- **Visibility**: `private` (feature-specific) or `internal`/`public` if in `core/ui/`
- **Responsibilities**: single, focused piece of UI — one card, one list item, one button
- **Always**: first parameter is `modifier: Modifier = Modifier`
- **Always**: stateless — receive data and callbacks, own no state
- **Never**: call `koinViewModel()`, `collectAsStateWithLifecycle()`, or perform side effects

```kotlin
@Composable
private fun ExampleItemComponent(
    modifier: Modifier = Modifier,
    item: ExampleUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        /* content */
    }
}
```

---

## Material 3 Rules

- **Colors**: always use `MaterialTheme.colorScheme.*` — never hardcode `Color(0xFF…)`
- **Typography**: always use `MaterialTheme.typography.*` — never hardcode `fontSize`
- **Shapes**: always use `MaterialTheme.shapes.*`
- **Elevation**: use `CardDefaults.cardElevation()`, `ButtonDefaults.*` — never raw `Dp` for shadow
- **Dynamic color**: supported via the app theme — do not override at component level

---

## Modifier Rules

- `Modifier` is always the **first parameter** of every `@Composable` with `Modifier` as default value
- Chain modifiers in this order: size → padding → background/border → clickable → semantics
- Never use `Modifier.fillMaxSize()` on a component that is meant to be reusable
- Pass `modifier` through to the root layout element of the composable

---

## Images — Coil 3

Always use `AsyncImage` from Coil 3 for network images. Never use `Image` with a URL string.

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalPlatformContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    contentDescription = stringResource(Res.string.recipe_image_description),
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .clip(MaterialTheme.shapes.medium)
)
```

- Always provide a meaningful `contentDescription` (see accessibility instructions)
- Always set `contentScale` explicitly
- Use `placeholder` and `error` parameters for graceful degradation

---

## UI State Management

- The `{Name}State` data class is the single source of truth for the UI.
- Use `isLoading: Boolean`, `error: String?` (or a sealed error type), and the data fields.
- Never use multiple `StateFlow`s in a ViewModel — combine everything into one `{Name}State`.
- Handle all four states explicitly: **Loading**, **Success** (with data), **Error**, **Empty**.

```kotlin
data class ExampleState(
    val isLoading: Boolean = false,
    val items: List<ExampleUiModel> = emptyList(),
    val error: String? = null
) {
    val isEmpty: Boolean get() = !isLoading && error == null && items.isEmpty()
}
```

---

## String Resources

- Never hardcode user-visible strings in Composables.
- Use `stringResource(Res.string.key)` from Compose Multiplatform resources.
- All strings are declared in `commonMain/composeResources/values/strings.xml`.

---

## Forbidden in Composables

- `collectAsState()` → use `collectAsStateWithLifecycle()`
- `koinViewModel()` outside of a `Root` composable
- `remember { mutableStateOf(...) }` for state that belongs in the ViewModel
- Hardcoded colors, font sizes, or dimensions
- `LaunchedEffect` outside of a `Root` composable (except animation-specific effects)
- Direct calls to UseCases, Repositories, or DataSources
