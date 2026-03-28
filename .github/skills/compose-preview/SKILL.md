---
name: compose-preview
description: Best practices for implementing Compose Previews, including conventions for `@Preview` functions, `PreviewParameterProvider` usage, and naming guidelines for page, component, and bottom sheet previews.
---

# Compose Preview Skill

## Overview

This skill defines the Compose Preview conventions for the ChefMate Kotlin Multiplatform project. Previews allow rapid UI iteration without running the full app. All preview code lives in `commonMain` and uses `@Preview` from `androidx.compose.ui.tooling.preview`.

> **Note**: The `@Preview` annotation renders in the Android IDE tooling panel. Since ChefMate uses Compose Multiplatform with shared UI in `commonMain`, the previewed composable runs identically on both Android and iOS.

---

## Imports

Always use these imports as needed:

```kotlin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
```

---

## General Rules

- **Every composable file must have at least one `@Preview`** — pages, design system components, feature components, and bottom sheet content.
- Preview composables are always `private` and annotated with `@Preview`.
- Preview composables are placed at the **bottom** of the file, after all non-preview composables.
- `PreviewParameterProvider` classes are `private` and placed **directly above** the `@Preview` function that uses them.
- Always wrap preview content in `ChefMateTheme { }` to render with the correct colors, typography, and shapes.
- Use `{ /* no-op */ }` for callback/lambda parameters.
- Use default-constructed state (`<Name>State()`) for simple state parameters.
- Use `<Name>NavigatorImpl()` (no-arg constructor, null controller) for navigator parameters.
- Never include ViewModel or Koin references in previews — previews target the **Page** level (or lower), not the **Root** level.
- Use `@PreviewParameter` with a `PreviewParameterProvider` to preview **multiple state variations** from a single preview function.

---

## Naming Conventions

| Element | Pattern | Example |
|---------|---------|---------|
| Feature page preview | `<Name>PagePreview` | `HomePagePreview` |
| Design system component preview | `<ComponentName>Preview` | `CMTopAppBarPreview` |
| Feature component preview | `<ComponentName>Preview` | `IngredientItemPreview` |
| Bottom sheet content preview | `<SheetName>ContentPreview` | `CreateIngredientBottomSheetContentPreview` |
| State provider | `<Name>StatePreviewProvider` | `SignInStatePreviewProvider` |
| UI data provider | `<Name>PreviewProvider` | `IngredientItemPreviewProvider` |
| Boolean provider | `BooleanPreviewProvider` | Shared — reusable across features |
| Multi-param wrapper | `<Name>PreviewData` | `CMButtonPreviewData` |

---

## PreviewParameterProvider

Use `PreviewParameterProvider<T>` to generate **multiple preview variants** from a single `@Preview` function. This avoids duplicating preview functions for each visual state.

### When to use

- The composable has **distinct visual states** (loading, content, error, empty).
- The composable has a **boolean toggle** that changes appearance (e.g., `isLoading`).
- The composable renders **different data configurations** (e.g., with/without optional fields, create vs edit mode).

### When NOT to use

- The composable has only **one meaningful visual state** (e.g., a static logo, a simple top app bar).
- The preview is already concise with a single default-constructed state.

### Provider structure

Providers follow a structured layout with three sections:

1. **Mock data** — Reusable `private` properties for sample values (strings, domain models, UI data).
2. **Sequences** — Named `private` properties, each building a distinct state variant from the mocks.
3. **`values`** — The `override val values` with a `get() = sequenceOf(…)` accessor referencing the named sequences.

Organize sections with `=`-separator comment blocks.

```kotlin
private class <Name>StatePreviewProvider : PreviewParameterProvider<<Name>State> {

    // =============================================================================================
    //  Mock Data
    // =============================================================================================

    private val titleMock = "Grandma's Apple Pie"
    private val ingredientMock = RecipeIngredientUiData(
        id = "1",
        name = "Flour",
        quantity = "2",
        unit = "cups",
        orderIndex = 0
    )

    // =============================================================================================
    //  Sequences
    // =============================================================================================

    private val loading = <Name>State(
        contentView = <Name>ContentView.Loading
    )

    private val content = <Name>State(
        contentView = <Name>ContentView.Content,
        title = titleMock
    )

    private val populated = <Name>State(
        contentView = <Name>ContentView.Content,
        title = titleMock,
        ingredients = listOf(ingredientMock).toImmutableList()
    )

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<<Name>State>
        get() = sequenceOf(
            loading,
            content,
            populated
        )
}
```

### Rules

- Extract repeated sample values into `private` mock properties at the top of the class — avoid duplicating literals across sequences.
- Name each sequence after the visual state it represents (e.g., `loading`, `content`, `emptyState`, `withErrors`, `populated`).
- Use `get() = sequenceOf(…)` for the `values` property — it is lazy and memory-efficient.
- Provide **meaningful, distinct variants** — don't add redundant similar entries.
- Cover edge cases: empty state, loading state, populated state, error/validation state, max-length content.
- Use realistic sample text (e.g., `"Chicken Thighs"`, `"john.doe@gmail.com"`) — not `"test"` or `"abc"`.
- Limit to **2–4 variants** per provider to keep the preview panel manageable.
- A `@Preview` function can only have **one** `@PreviewParameter`. If you need to vary multiple parameters, create a wrapper `data class`.

---

## Page Previews

Page previews render the `<Name>Page` composable (private, Scaffold-level) — **not** `<Name>Root` (which depends on ViewModel/Koin).

### Simple page — no provider needed

When the page has a trivial default state:

```kotlin
@Preview
@Composable
private fun HomePagePreview() {
    ChefMateTheme {
        HomePage(
            state = HomeState(),
            onAction = { /* no-op */ },
            navigator = HomeNavigatorImpl()
        )
    }
}
```

### Page with multiple visual states — use PreviewParameterProvider

When the page has meaningful state variations (loading, populated, empty):

```kotlin
private class SignInStatePreviewProvider : PreviewParameterProvider<SignInState> {

    // =============================================================================================
    //  Mock Data
    // =============================================================================================

    private val emailMock = "john.doe@gmail.com"
    private val passwordMock = "Azerty123!"

    // =============================================================================================
    //  Sequences
    // =============================================================================================

    private val empty = SignInState()

    private val filled = SignInState(
        email = emailMock,
        password = passwordMock,
        isLoading = false
    )

    private val loading = SignInState(
        email = emailMock,
        password = passwordMock,
        isLoading = true
    )

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<SignInState>
        get() = sequenceOf(
            empty,
            filled,
            loading
        )
}

@Preview
@Composable
private fun SignInPagePreview(
    @PreviewParameter(SignInStatePreviewProvider::class) state: SignInState
) {
    ChefMateTheme {
        SignInPage(
            state = state,
            onAction = { /* no-op */ },
            navigator = SignInNavigatorImpl()
        )
    }
}
```

### Dark mode variant

Use `ChefMateTheme(isDarkMode = …)` to preview a specific theme mode:

```kotlin
@Preview
@Composable
private fun SignInPageDarkPreview() {
    ChefMateTheme(isDarkMode = true) {
        SignInPage(
            state = SignInState(),
            onAction = { /* no-op */ },
            navigator = SignInNavigatorImpl()
        )
    }
}
```

---

## Design System Component Previews

Design system components (`CM`-prefixed, in `designsystem/component/`) include their own preview at the bottom of the file.

### Simple component — no provider needed

```kotlin
@Preview
@Composable
private fun CMTopAppBarPreview() {
    ChefMateTheme {
        CMTopAppBar(
            title = "Top App Bar",
            onNavigationClick = { /* no-op */ }
        )
    }
}
```

### Component with visual variants — use PreviewParameterProvider

```kotlin
private class CMButtonPreviewProvider : PreviewParameterProvider<Boolean> {

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<Boolean>
        get() = sequenceOf(false, true)
}

@Preview
@Composable
private fun CMButtonPreview(
    @PreviewParameter(CMButtonPreviewProvider::class) isLoading: Boolean
) {
    ChefMateTheme {
        CMButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Get Started",
            isLoading = isLoading,
            onClick = { /* no-op */ }
        )
    }
}
```

### Multiple parameters — use a wrapper data class

When varying more than one parameter, create a `private data class` and a provider for it:

```kotlin
private data class CMButtonPreviewData(
    val label: String,
    val isLoading: Boolean
)

private class CMButtonPreviewDataProvider : PreviewParameterProvider<CMButtonPreviewData> {

    // =============================================================================================
    //  Mock Data
    // =============================================================================================

    private val signInLabelMock = "Sign In"
    private val getStartedLabelMock = "Get Started"

    // =============================================================================================
    //  Sequences
    // =============================================================================================

    private val idle = CMButtonPreviewData(
        label = signInLabelMock,
        isLoading = false
    )

    private val loading = CMButtonPreviewData(
        label = signInLabelMock,
        isLoading = true
    )

    private val altLabel = CMButtonPreviewData(
        label = getStartedLabelMock,
        isLoading = false
    )

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<CMButtonPreviewData>
        get() = sequenceOf(
            idle,
            loading,
            altLabel
        )
}

@Preview
@Composable
private fun CMButtonPreview(
    @PreviewParameter(CMButtonPreviewDataProvider::class) data: CMButtonPreviewData
) {
    ChefMateTheme {
        CMButton(
            modifier = Modifier.fillMaxWidth(),
            label = data.label,
            isLoading = data.isLoading,
            onClick = { /* no-op */ }
        )
    }
}
```

### Background context

When previewing a component that needs background context (e.g., a text field that blends with the surface), apply a background modifier:

```kotlin
@Preview
@Composable
private fun CMTextFieldPreview() {
    ChefMateTheme {
        CMTextField(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            value = "",
            onValueChange = { /* no-op */ },
            label = "Email address",
            hint = "Enter text here"
        )
    }
}
```

---

## Feature Component Previews

Feature-scoped components (in `feature/<name>/component/`) include their own preview.

### Simple component preview

```kotlin
@Preview
@Composable
private fun InstructionItemPreview() {
    ChefMateTheme {
        InstructionItem(
            title = "Instruction title",
            instruction = "Lorem ipsum dolor sit amet",
            index = 1,
            onEditInstructionClick = { /* no-op */ }
        )
    }
}
```

### Component with data variations — use PreviewParameterProvider

```kotlin
private class IngredientItemPreviewProvider : PreviewParameterProvider<RecipeIngredientUiData> {

    // =============================================================================================
    //  Sequences
    // =============================================================================================

    private val withNotes = RecipeIngredientUiData(
        id = "1",
        name = "Chicken Thighs",
        quantity = "2",
        unit = "lbs",
        notes = "Bone-in, skin-on preferred",
        orderIndex = 0
    )

    private val withoutNotes = RecipeIngredientUiData(
        id = "2",
        name = "Olive Oil",
        quantity = "3",
        unit = "tbsp",
        notes = null,
        orderIndex = 1
    )

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<RecipeIngredientUiData>
        get() = sequenceOf(
            withNotes,
            withoutNotes
        )
}

@Preview
@Composable
private fun IngredientItemPreview(
    @PreviewParameter(IngredientItemPreviewProvider::class) ingredient: RecipeIngredientUiData
) {
    ChefMateTheme {
        IngredientItem(
            ingredient = ingredient,
            onEditClick = { /* no-op */ }
        )
    }
}
```

### Component with ImmutableList

For components accepting `ImmutableList`, construct sample data with `.toImmutableList()` or `persistentListOf()`:

```kotlin
@Preview
@Composable
private fun IngredientMainSectionContainerPreview() {
    ChefMateTheme {
        IngredientMainSectionContainer(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            mainIngredients = listOf(
                RecipeIngredientUiData(
                    id = "1",
                    name = "Flour",
                    quantity = "2",
                    unit = "cups",
                    orderIndex = 0
                )
            ).toImmutableList(),
            onAddSectionClick = { /* no-op */ },
            onEditIngredientClick = { /* no-op */ },
            onAddMainIngredientClick = { /* no-op */ }
        )
    }
}
```

---

## Bottom Sheet Content Previews

Bottom sheet previews target the **private content composable** (e.g., `CreateInstructionBottomSheetContent`), not the public sheet wrapper (which depends on `ModalBottomSheet`).

Apply a surface background to simulate the sheet container.

### Simple sheet — no provider needed

```kotlin
@Preview
@Composable
private fun CreateInstructionBottomSheetContentPreview() {
    ChefMateTheme {
        CreateInstructionBottomSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            instruction = null,
            onDismissRequest = { /* no-op */ },
            onConfirmClick = { _, _ -> /* no-op */ }
        )
    }
}
```

### Sheet with create/edit modes — use PreviewParameterProvider

```kotlin
private class InstructionPreviewProvider : PreviewParameterProvider<RecipeInstructionUiData?> {

    // =============================================================================================
    //  Mock Data
    // =============================================================================================

    private val instructionMock = RecipeInstructionUiData(
        id = "1",
        title = "Preheat Oven",
        instruction = "Preheat the oven to 180°C.",
        orderIndex = 0
    )

    // =============================================================================================
    //  Sequences
    // =============================================================================================

    private val createMode: RecipeInstructionUiData? = null

    private val editMode: RecipeInstructionUiData? = instructionMock

    // =============================================================================================
    //  Values
    // =============================================================================================

    override val values: Sequence<RecipeInstructionUiData?>
        get() = sequenceOf(
            createMode,
            editMode
        )
}

@Preview
@Composable
private fun CreateInstructionBottomSheetContentPreview(
    @PreviewParameter(InstructionPreviewProvider::class) instruction: RecipeInstructionUiData?
) {
    ChefMateTheme {
        CreateInstructionBottomSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            instruction = instruction,
            onDismissRequest = { /* no-op */ },
            onConfirmClick = { _, _ -> /* no-op */ }
        )
    }
}
```

---

## Preview Data Guidelines

| Data Type | How to Provide |
|-----------|---------------|
| State | Default constructor `<Name>State()` or via `PreviewParameterProvider` for multiple states |
| Navigator | No-arg impl: `HomeNavigatorImpl()` |
| Callbacks / lambdas | `{ /* no-op */ }` or `{ _, _ -> /* no-op */ }` for multi-param |
| `ImmutableList` | `persistentListOf()` (empty) or `listOf(…).toImmutableList()` (with data) |
| Nullable types | `null` for empty/default, or a sample instance for populated state |
| String fields | Realistic sample text (e.g., `"Chicken Thighs"`, `"chef@example.com"`) |
| Boolean toggles | Via `BooleanPreviewProvider` or a typed `PreviewParameterProvider` |

---

## What NOT to Preview

- **`<Name>Root`** — Depends on `koinViewModel()` and `ObserveAsEvent`, which do not work in preview.
- **`NavHost` composables** (`MainNavHost`, `AuthenticationNavHost`) — Depend on runtime navigation and `SnackbarController`.
- **`ChefMateEntrypoint`** — Depends on `AuthenticationStatus` from a ViewModel.
- **Public `BottomSheet` wrappers** — Depend on `ModalBottomSheet` and `SheetState` which require a running composition. Preview the **private content composable** instead.

---

## Checklist When Adding a Preview

1. Place `PreviewParameterProvider` classes (if any) **directly above** the `@Preview` function.
2. Place `@Preview` + `@Composable` at the **bottom** of the file.
3. Make the preview function and the provider class `private`.
4. Name it `<ComponentName>Preview` or `<Name>PagePreview`.
5. Wrap in `ChefMateTheme { }`.
6. Use default/sample data — no ViewModel, no Koin, no NavController.
7. Use `@PreviewParameter` when the composable has **multiple meaningful visual states**.
8. Add background modifier if the component needs surface context.
9. Verify the preview renders in the IDE panel without errors.
