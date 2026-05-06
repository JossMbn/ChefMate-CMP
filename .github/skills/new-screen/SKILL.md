---
name: new-screen
description: >-
  Creates a complete MVI screen for ChefMate following the Root → Page → Content → Component
  hierarchy. Use when asked to "create a screen", "add a page", "new screen for", "add the
  {name} screen", or any request to build a new UI screen or page.
---

# Skill: New Screen

Creates all files needed for a complete ChefMate screen following the project architecture.

## Files to Create

For a screen named `{Name}` in feature `{feature}`:

```
feature/{feature}/presentation/
├── navigation/
│   └── {Name}Navigation.kt          ← Route, Navigator, NavGraphBuilder extension
├── screen/
│   ├── {Name}Root.kt                ← Root composable + ViewModel + State/Action/Event
│   └── {Name}ViewModel.kt           ← ViewModel
└── preview/
    └── {Name}Preview.kt             ← Previews only
```

---

## Step 1 — Identify the Screen

Ask (or infer from context):
- What is the screen name? (e.g. `RecipeDetail`, `CreateCollection`, `Home`)
- Which feature does it belong to? (e.g. `recipe`, `collection`, `home`)
- Does it need one-shot events (navigation, snackbar)? → `{Name}Event` sealed interface
- Does it receive arguments from navigation? → `@Serializable data class {Name}Route(val id: String)`
- Which UseCases does it use?

---

## Step 2 — Create `{Name}Navigation.kt`

```kotlin
// feature/{feature}/presentation/navigation/{Name}Navigation.kt

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object {Name}Route  // or data class if it has navigation arguments

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface {Name}Navigator {
    fun navigateBack()
    // Add feature-specific navigation functions
    // fun navigateTo{Target}(id: String)
}

class {Name}NavigatorImpl(
    private val controller: NavController? = null
) : {Name}Navigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.{nameCamelCase}Page(
    controller: NavController
) {
    composable<{Name}Route> {
        {Name}Root(
            navigator = {Name}NavigatorImpl(controller = controller)
        )
    }
}
```

---

## Step 3 — Create `{Name}Root.kt`

This file contains: State, Action, Event (if needed), Root, Page, and Content composables.

```kotlin
// feature/{feature}/presentation/screen/{Name}Root.kt

// ==================================================================================
//  MVI Contracts
// ==================================================================================

data class {Name}State(
    val isLoading: Boolean = false,
    val error: String? = null
    // Add state fields
)

sealed interface {Name}Action {
    // Add user actions
    // data object OnBackClick : {Name}Action
    // data class OnItemClick(val id: String) : {Name}Action
}

sealed interface {Name}Event {
    // Add one-shot events (only if needed)
    // data object NavigateBack : {Name}Event
}

// ==================================================================================
//  Root
// ==================================================================================

@Composable
fun {Name}Root(
    viewModel: {Name}ViewModel = koinViewModel(),
    navigator: {Name}Navigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                // Handle events
            }
        }
    }

    {Name}Page(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

// ==================================================================================
//  Page
// ==================================================================================

@Composable
private fun {Name}Page(
    state: {Name}State,
    onAction: ({Name}Action) -> Unit,
    navigator: {Name}Navigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        {Name}Content(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction
        )
    }
}

// ==================================================================================
//  Content
// ==================================================================================

@Composable
private fun {Name}Content(
    modifier: Modifier = Modifier,
    state: {Name}State,
    onAction: ({Name}Action) -> Unit
) {
    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            // Error state
        }
        else -> {
            // Main content
        }
    }
}
```

---

## Step 4 — Create `{Name}ViewModel.kt`

```kotlin
// feature/{feature}/presentation/screen/{Name}ViewModel.kt

class {Name}ViewModel(
    // Inject UseCases here
) : ViewModel() {

    private val _event = MutableSharedFlow<{Name}Event>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow({Name}State())
    val state = _state
        .onStart { loadInitialData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = {Name}State()
        )

    fun onAction(action: {Name}Action) {
        when (action) {
            // Handle actions
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Call UseCases here
            _state.update { it.copy(isLoading = false) }
        }
    }
}
```

If the screen observes a reactive repository (Flow), add the subscription in `init {}`:

```kotlin
init {
    viewModelScope.launch {
        observeCollectionsUseCase()
            .collect { collections ->
                _state.update { it.copy(collections = collections) }
            }
    }
}
```

---

## Step 5 — Create `{Name}Preview.kt`

```kotlin
// feature/{feature}/presentation/preview/{Name}Preview.kt

class {Name}StateProvider : PreviewParameterProvider<{Name}State> {
    override val values = sequenceOf(
        {Name}State(isLoading = true),
        {Name}State(/* success state with data */),
        {Name}State(error = "Something went wrong")
    )
}

@PreviewLightDark
@Composable
private fun {Name}PagePreview(
    @PreviewParameter({Name}StateProvider::class) state: {Name}State
) {
    ChefMateTheme {
        {Name}Page(
            state = state,
            onAction = {},
            navigator = {Name}NavigatorImpl()
        )
    }
}
```

---

## Step 6 — Register in Koin

Add the ViewModel to the feature's Koin module:

```kotlin
// feature/{feature}/{Feature}Module.kt
val {feature}Module = module {
    // ... existing declarations
    viewModelOf(::{Name}ViewModel)
}
```

---

## Step 7 — Register in Navigation Graph

Add the new page to the root NavHost or the relevant feature navigation:

```kotlin
// In the NavHost / root navigation file
{nameCamelCase}Page(controller = navController)
```

---

## Checklist

- [ ] `{Name}Navigation.kt` — Route, Navigator interface, NavigatorImpl, NavGraphBuilder extension
- [ ] `{Name}Root.kt` — State (data class), Action (sealed interface), Event (sealed interface if needed), Root, Page, Content
- [ ] `{Name}ViewModel.kt` — ViewModel with `onAction`, `state`, `event`
- [ ] `{Name}Preview.kt` — PreviewParameterProvider + `@PreviewLightDark` previews in ChefMateTheme
- [ ] ViewModel registered in the feature Koin module
- [ ] Route registered in the navigation graph
- [ ] No `collectAsState()` — only `collectAsStateWithLifecycle()`
- [ ] No business logic in composables
- [ ] `Modifier` as first parameter with default value in Content and all Components
