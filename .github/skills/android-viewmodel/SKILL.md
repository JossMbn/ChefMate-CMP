---
name: android-viewmodel
description: Best practices for implementing Android ViewModels, specifically focused on StateFlow for UI state and SharedFlow / Channel for one-off events.
---

# Android ViewModel Skill

## Overview

This skill defines how ViewModels are structured in the ChefMate project following the **MVI (Model-View-Intent)** pattern with `androidx.lifecycle.ViewModel`.

---

## ViewModel Structure

Every ViewModel extends `androidx.lifecycle.ViewModel` and is organized into four sections with comment block separators:

```kotlin
class <Name>ViewModel(
    private val someUseCase: SomeUseCase
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    // ...

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    // ...

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    // ...

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    // ...
}
```

---

## State Exposure

### Simple state — single `MutableStateFlow`

Use when the state is a flat data class mutated via `.update { it.copy(...) }`:

```kotlin
private val _state = MutableStateFlow(<Name>State())
val state = _state
    .onStart { loadData() }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = <Name>State()
    )
```

### Complex state — `combine` multiple flows

Use when the state is composed from several independent data streams:

```kotlin
private val _isLoading = MutableStateFlow(false)
private val _items = MutableStateFlow<ImmutableList<ItemUiData>>(persistentListOf())
private val _dialogState = MutableStateFlow<DialogState?>(null)

val state = combine(
    _isLoading,
    _items,
    _dialogState
) { isLoading, items, dialogState ->
    <Name>State(
        isLoading = isLoading,
        items = items,
        dialogState = dialogState
    )
}
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = <Name>State()
    )
```

### Rules

- Always use `SharingStarted.Lazily` — state is only computed when first collected.
- The `initialValue` must match the default-constructed state: `<Name>State()`.
- Use `.onStart { }` to trigger initial data loading when using a single `MutableStateFlow`.

---

## Event Channel

Use for **one-shot side-effects** (navigation triggers, success confirmations).

### `Channel` (guaranteed delivery)

```kotlin
private val _event = Channel<<Name>Event>()
val event = _event.receiveAsFlow()
```

### `MutableSharedFlow` (simple broadcast)

```kotlin
private val _event = MutableSharedFlow<<Name>Event>()
val event = _event.asSharedFlow()
```

### Sending events

```kotlin
// From a coroutine (Channel)
_event.send(<Name>Event.OperationSuccess)

// From a coroutine (SharedFlow)
_event.emit(<Name>Event.OperationSuccess)
```

---

## Action Handling

A single public `onAction` function dispatches all user intents via a `when` expression:

```kotlin
fun onAction(action: <Name>Action) {
    when (action) {
        is <Name>Action.OnTitleChange -> handleTitleChange(action.newTitle)
        <Name>Action.OnSaveClick -> save()
        <Name>Action.OnDismissDialog -> dismissDialog()
    }
}
```

### Rules

- Each `when` branch delegates to a **private method** — keep `onAction` concise.
- Group action branches with inline comments matching the Action sealed interface groups.
- Use named arguments when delegating to private methods with multiple parameters.

---

## State Mutation

- Use `_state.update { it.copy(...) }` for atomic, thread-safe state updates.
- Never read `_state.value`, modify it, and reassign — always use `.update { }`.
- For individual sub-flows (e.g., `_items`), use `.update { }` or `.emit()` as appropriate.

```kotlin
private fun handleTitleChange(newTitle: String) {
    _state.update { it.copy(title = newTitle) }
}
```

---

## Async Operations

- Launch coroutines via `viewModelScope.launch { }`.
- Toggle loading state **before** the async call and reset it in `.also { }` **after**.
- Handle results via `Result<T>` with `.onSuccess { }` and `.onFailure { }`.
- Report errors via `SnackbarController.sendError(error = error)`.

```kotlin
private fun save() {
    viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        someUseCase(/* params */)
            .onSuccess {
                _event.send(<Name>Event.SaveSuccess)
            }
            .onFailure { error ->
                SnackbarController.sendError(error = error)
            }
            .also {
                _state.update { it.copy(isLoading = false) }
            }
    }
}
```

---

## Dependency Injection

- Use **constructor injection** — Koin resolves all dependencies automatically.
- Register ViewModels in `di/presentation/ViewModelModule.kt` using `viewModelOf(::…)`.
- Obtain ViewModels in composables via `koinViewModel()`.

```kotlin
// ViewModelModule.kt
viewModelOf(::HomeViewModel)

// HomePage.kt
@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: HomeNavigator
) { /* ... */ }
```

---

## Dialog / Sheet State Management

- Use a nullable `MutableStateFlow<DialogState?>` to drive dialog visibility.
- Set to a subtype to show, set to `null` to dismiss.
- Each dialog subtype carries the data needed to populate the dialog UI.

```kotlin
private val _dialogState = MutableStateFlow<DialogState?>(null)

private fun showEditDialog(itemId: String) {
    val item = findItem(itemId)
    _dialogState.update { DialogState.EditItem(item = item) }
}

private fun dismissDialog() {
    _dialogState.update { null }
}
```

---

## Naming Conventions

| Element | Pattern | Example |
|---------|---------|---------|
| ViewModel class | `<Name>ViewModel` | `HomeViewModel` |
| State class | `<Name>State` | `HomeState` |
| Action sealed interface | `<Name>Action` | `HomeAction` |
| Event sealed interface | `<Name>Event` | `HomeEvent` |
| Dialog state | `<Name>DialogState` | `ManualRecipeCreationDialogState` |
| ContentView enum | `<Name>ContentView` | `HomeContentView` |
