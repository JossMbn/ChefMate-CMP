---
name: kotlin-concurrency-expert
description: Kotlin Coroutines review and remediation. Use when asked to review concurrency usage, fix coroutine-related bugs, improve thread safety, or resolve lifecycle issues in Kotlin Multiplatform code.
---

# Kotlin Concurrency Expert Skill

## Overview

This skill defines the coroutine and concurrency conventions used in the ChefMate project. All async work is powered by Kotlin Coroutines with structured concurrency patterns.

---

## Coroutine Scopes

### `viewModelScope`

- The **only** coroutine scope used for launching work in ViewModels.
- Automatically cancelled when the ViewModel is cleared.
- Never create custom `CoroutineScope` instances inside ViewModels.

```kotlin
class HomeViewModel : ViewModel() {
    private fun loadData() {
        viewModelScope.launch {
            // async work here
        }
    }
}
```

### Composable scopes

- Use `rememberCoroutineScope()` for launching coroutines from composable event handlers (e.g., snackbar dismissal in NavHosts).
- Use `LaunchedEffect(key) { }` for side effects tied to composition lifecycle.

```kotlin
val scope = rememberCoroutineScope()
scope.launch {
    snackbarHostState.showSnackbar(message = "Error occurred")
}
```

---

## Flow Patterns

### StateFlow — UI state

- Use `MutableStateFlow` for mutable state, expose as `StateFlow` via `.stateIn()`.
- Always provide `SharingStarted.Lazily` — only starts when first collected.
- Use `.onStart { }` to trigger initial data loading.

```kotlin
private val _state = MutableStateFlow(HomeState())
val state = _state
    .onStart { loadData() }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeState()
    )
```

### Combining flows

- Use `combine(flow1, flow2, ...) { a, b, ... -> }` to derive state from multiple independent sources.
- Pass the result through `.stateIn()` with `SharingStarted.Lazily`.

```kotlin
val state = combine(
    _isLoading,
    _recipe,
    _dialogState
) { isLoading, recipe, dialogState ->
    ManualRecipeCreationState(
        isCreatingRecipe = isLoading,
        recipe = recipe,
        dialogState = dialogState
    )
}
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ManualRecipeCreationState()
    )
```

### SharedFlow — one-shot events (broadcast)

```kotlin
private val _event = MutableSharedFlow<<Name>Event>()
val event = _event.asSharedFlow()

// Emit from a coroutine
_event.emit(<Name>Event.SomeEvent)
```

### Channel — one-shot events (guaranteed delivery)

```kotlin
private val _event = Channel<<Name>Event>()
val event = _event.receiveAsFlow()

// Send from a coroutine
_event.send(<Name>Event.SomeEvent)
```

### When to use which

| Pattern | Use Case | Guarantee |
|---------|----------|-----------|
| `MutableSharedFlow` | Simple event broadcasting | Events may be lost if no collector |
| `Channel` | Guaranteed delivery events | Buffers until collected |
| `MutableStateFlow` | Continuous UI state | Always has current value |

---

## State Updates

- **Always** use `MutableStateFlow.update { }` for thread-safe state mutations.
- Never read `.value`, modify, and reassign — this is not atomic.

```kotlin
// ✅ Thread-safe
_state.update { it.copy(isLoading = true) }

// ❌ Race condition
val current = _state.value
_state.value = current.copy(isLoading = true)
```

- Use `.emit()` for `MutableStateFlow` only when inside a coroutine and when you want to suspend.

---

## Structured Concurrency

- Never use `GlobalScope` — always use `viewModelScope` or a properly scoped coroutine scope.
- Never launch fire-and-forget coroutines without a scope.
- Cancellation is cooperative: long-running operations should check `isActive` or use suspending functions that respect cancellation.

---

## Error Handling in Coroutines

### `Result<T>` pattern

All data/domain operations return `Result<T>`. Handle in the ViewModel:

```kotlin
viewModelScope.launch {
    _state.update { it.copy(isLoading = true) }
    someUseCase(params)
        .onSuccess { result ->
            _event.send(Event.Success)
        }
        .onFailure { error ->
            SnackbarController.sendError(error = error)
        }
        .also {
            _state.update { it.copy(isLoading = false) }
        }
}
```

### `runCatching { }` in data sources

Wrap Supabase/Ktor calls in `runCatching { }` to produce `Result<T>`:

```kotlin
suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> = runCatching {
    supabaseClient.from("recipes")
        .select { filter { eq("id", recipeId) } }
        .decodeSingle<RecipeDto>()
        .toDomain()
}
```

---

## Lifecycle-Aware Collection

### In composables — `collectAsStateWithLifecycle()`

```kotlin
val state by viewModel.state.collectAsStateWithLifecycle()
```

- Automatically stops collection when the composable leaves the `STARTED` lifecycle state.
- Always use `by` delegation.

### One-shot events — `ObserveAsEvent`

```kotlin
ObserveAsEvent(viewModel.event) { event ->
    when (event) {
        Event.Success -> navigator.navigateBack()
    }
}
```

- Uses `repeatOnLifecycle(Lifecycle.State.STARTED)` under the hood.
- Place in `Root` composables, before calling `Page`.

---

## Snackbar Controller — Cross-Scope Communication

`SnackbarController` uses a `Channel` to communicate errors from ViewModels to the NavHost-level composable:

```kotlin
// ViewModel (producer)
SnackbarController.sendError(error = throwable)

// NavHost (consumer)
ObserveAsEvent(SnackbarController.event) { event ->
    scope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(message = event.message.asStringSuspend())
    }
}
```

---

## Dispatcher Usage

- Do **not** explicitly switch dispatchers in ViewModels — `viewModelScope` uses `Dispatchers.Main.immediate` by default.
- Supabase/Ktor calls are inherently suspend functions that handle their own threading.
- If CPU-intensive work is needed, use `withContext(Dispatchers.Default) { }` explicitly.

---

## Common Mistakes to Avoid

| ❌ Don't | ✅ Do |
|----------|------|
| Use `GlobalScope.launch` | Use `viewModelScope.launch` |
| Create custom `CoroutineScope` in ViewModel | Use `viewModelScope` |
| Read `.value` and reassign on `MutableStateFlow` | Use `.update { }` |
| Use `try/catch` in ViewModel for use case calls | Use `Result<T>` with `.onSuccess`/`.onFailure` |
| Forget `.also { }` to reset loading state | Always reset loading in `.also { }` after async call |
| Use `Dispatchers.IO` explicitly for Supabase calls | Supabase/Ktor handles threading internally |
| Collect `StateFlow` without lifecycle awareness | Use `collectAsStateWithLifecycle()` |
