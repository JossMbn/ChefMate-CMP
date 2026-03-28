---
name: compose-ui
description: Best practices for building UI with Jetpack Compose, focusing on state hoisting, detailed performance optimizations, and theming. Use this when writing or refactoring Composable functions.
---

# Compose UI Skill

## Overview

This skill defines the Compose Multiplatform UI conventions for the ChefMate project. All UI code lives in `commonMain` and targets both Android and iOS through Jetpack Compose Multiplatform.

---

## Page File Structure

Each feature screen is a single `<Name>Page.kt` file containing **four composables** in order:

### 1. `<Name>Root` (public)

Wires ViewModel, state observation, event observation, and navigator.

```kotlin
@Composable
fun <Name>Root(
    viewModel: <Name>ViewModel = koinViewModel(),
    navigator: <Name>Navigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Optional: observe one-shot events
    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            <Name>Event.Success -> navigator.navigateBack()
        }
    }

    <Name>Page(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}
```

### 2. `<Name>Page` (private)

Receives `state`, `onAction` lambda, and `navigator`. Contains `Scaffold` with layout structure.

```kotlin
@Composable
private fun <Name>Page(
    state: <Name>State,
    onAction: (<Name>Action) -> Unit,
    navigator: <Name>Navigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "Page Title",
                onNavigationClick = { navigator.navigateBack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        <Name>PageContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}
```

### 3. `<Name>PageContent` (private)

Inner content with `modifier: Modifier = Modifier` as first parameter.

```kotlin
@Composable
private fun <Name>PageContent(
    modifier: Modifier = Modifier,
    state: <Name>State,
    onAction: (<Name>Action) -> Unit,
    navigator: <Name>Navigator
) {
    // UI content here
}
```

### 4. `<Name>PagePreview` (private)

Preview at the bottom of the file using `ChefMateTheme` and default state.

> **Note**: `@Preview` uses `androidx.compose.ui.tooling.preview.Preview` which renders on Android. For shared Compose Multiplatform code, this is acceptable since the shared UI runs identically on both platforms.

```kotlin
@Preview
@Composable
private fun <Name>PagePreview() {
    ChefMateTheme {
        <Name>Page(
            state = <Name>State(),
            onAction = { /* no-op */ },
            navigator = <Name>NavigatorImpl()
        )
    }
}
```

---

## Theming

- **Always** use `MaterialTheme.colorScheme` tokens — never hardcode color values.
- Use `MaterialTheme.typography` for text styles.
- Use `MaterialTheme.shapes` for shape theming.
- The app theme is `ChefMateTheme` — supports light/dark via `isDarkMode` parameter.

```kotlin
// ✅ Correct
color = MaterialTheme.colorScheme.primary

// ❌ Wrong
color = Color(0xFF6200EE)
```

---

## State Observation

- Use `collectAsStateWithLifecycle()` from `androidx.lifecycle.compose` to observe `StateFlow` in composables.
- Always destructure with `by` delegation: `val state by viewModel.state.collectAsStateWithLifecycle()`.

---

## Event Observation

- Use `ObserveAsEvent(viewModel.event) { event -> ... }` from `core/presentation/` to collect one-shot events.
- Place `ObserveAsEvent` calls in `Root` composables, **before** calling the `Page` composable.

---

## Design System Components

- Reusable components live in `designsystem/component/` and are prefixed with `CM` (e.g., `CMTopAppBar`, `CMTextField`, `CMButton`).
- Feature-scoped components live in `feature/<name>/component/` and are **not** prefixed with `CM`.
- Use `painterResource(Res.drawable.ic_…)` from Compose Multiplatform resources for icons.

### Existing components

| Component | Location | Purpose |
|-----------|----------|---------|
| `CMTopAppBar` | `designsystem/component/` | CenterAligned top app bar with optional back nav + actions |
| `CMTextField` | `designsystem/component/textfield/` | Styled text field with label, hint, leading content |
| `CMButton` | `designsystem/component/button/` | Primary button with loading state |
| `AddTextButton` | `designsystem/component/button/` | Text button for "Add" actions |
| `AppLogo` | `designsystem/component/` | App logo with optional app name |
| `FieldLabelContainer` | `designsystem/component/` | Label wrapper for form fields |
| `BottomSheetContainer` | `designsystem/sheet/` | Wrapper for `ModalBottomSheet` with dismiss animation |

---

## Bottom Sheets & Dialogs

- Feature-specific sheets live in `feature/<name>/sheet/` and may contain a `component/` subfolder for sheet-specific sub-components.
- Wrap content in `BottomSheetContainer` from `designsystem/sheet/`.
- Visibility is driven by `dialogState` in the feature state (nullable sealed interface).
- Render conditionally via `when (state.dialogState) { ... }` inside the `Page` composable **after** `Scaffold`.

```kotlin
state.dialogState?.let { dialogState ->
    when (dialogState) {
        is DialogState.CreateItem -> {
            CreateItemBottomSheet(
                onDismissRequest = { onAction(OnDismissDialog) },
                onConfirmClick = { /* dispatch action */ }
            )
        }
    }
}
```

---

## Navigation in Composables

- Navigation is abstracted via `<Name>Navigator` interfaces — composables never access `NavController` directly.
- Use `navigator::methodName` for click handlers when the method takes no parameters.
- Use lambda `{ navigator.navigateToPage(args) }` when passing arguments.

```kotlin
// ✅ Method reference for no-arg navigation
IconButton(onClick = navigator::navigateToAccountPage) { /* ... */ }

// ✅ Lambda for navigation with arguments
onClick = { navigator.navigateToRecipeDetail(recipeId) }
```

---

## Compose Multiplatform Specifics

- All shared UI code goes in `commonMain`.
- Use `expect`/`actual` for platform-specific UI behavior (e.g., `ImagePicker` in `designsystem/provider/`).
- Use Compose Multiplatform resource system: `org.jetbrains.compose.resources` for strings, drawables, etc.
- Use `painterResource(Res.drawable.…)` instead of Android-specific resource loading.

---

## Modifier Conventions

- `Modifier` is always the **first** optional parameter with default `Modifier`.
- Chain modifiers in a readable order: layout → size → padding → appearance → interaction.
- Use `Modifier.fillMaxSize()` on root containers.
- Apply `Modifier.padding(innerPadding)` from `Scaffold`'s content lambda.

---

## List & Collection Rendering

- Use `ImmutableList` from `kotlinx.collections.immutable` for list state properties to optimize recomposition.
- Use `LazyColumn` / `LazyRow` with `items(key = { it.id })` for stable list rendering.
- Use `Arrangement.spacedBy(…)` for consistent spacing between list items.
- Use `PaddingValues` for content padding in lazy lists.

---

## Empty Lambdas

- Use `{ /* no-op */ }` for intentionally empty lambdas in previews and tests.
- Use `/* no-op */` comment for empty blocks.
