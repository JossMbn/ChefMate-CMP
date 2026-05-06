---
applyTo: "**/preview/**/*Preview.kt"
---

# Compose Preview Instructions

Based on official Google recommendations (Android Developers documentation, MAD Skills, Now in Android).

## File Location & Naming

- Previews live in a dedicated `preview/` package inside `presentation/`
- One preview file per screen or complex component: `{Name}Preview.kt`
- Never put previews in the same file as the composable implementation
- Never put previews in `Root` files — previews target `Page` and `Content` composables

```
feature/collection/presentation/
├── screen/
│   ├── CollectionRoot.kt
│   ├── CollectionPage.kt       ← implementation
│   └── CollectionViewModel.kt
└── preview/
    └── CollectionPreview.kt    ← all previews for this screen
```

---

## Required Annotations

Never use `@Preview` alone. Always use the multi-annotation approach:

```kotlin
@PreviewLightDark          // renders in both light and dark theme
@PreviewFontScale          // renders at multiple font scales (accessibility)
annotation class ChefMatePreview
```

Declare `ChefMatePreview` once in `core/ui/preview/ChefMatePreview.kt` and reuse it everywhere.

For screens with many distinct states, use individual `@PreviewLightDark` per state so
each state is clearly labelled in the preview panel.

---

## Always Wrap in App Theme

Every preview **must** be wrapped in `ChefMateTheme`. Never preview without the theme.

```kotlin
@ChefMatePreview
@Composable
private fun CollectionPagePreview() {
    ChefMateTheme {
        CollectionPage(
            state = CollectionState(),
            onAction = {},
            navigator = CollectionNavigatorImpl()
        )
    }
}
```

---

## PreviewParameterProvider for Multiple States

When a screen has distinct states (Loading, Success, Error, Empty), use a
`PreviewParameterProvider` instead of duplicating preview functions.

```kotlin
// In the preview file
class CollectionStateProvider : PreviewParameterProvider<CollectionState> {
    override val values = sequenceOf(
        CollectionState(isLoading = true),
        CollectionState(collections = PreviewData.collections),
        CollectionState(error = "Failed to load collections"),
        CollectionState(collections = emptyList())
    )
}

@PreviewLightDark
@Composable
private fun CollectionPagePreview(
    @PreviewParameter(CollectionStateProvider::class) state: CollectionState
) {
    ChefMateTheme {
        CollectionPage(
            state = state,
            onAction = {},
            navigator = CollectionNavigatorImpl()
        )
    }
}
```

---

## Preview Data Objects

Centralise fake data for previews in a `PreviewData` object inside the `preview/` package.
Never inline complex fake data directly in the preview annotation.

```kotlin
// feature/collection/presentation/preview/CollectionPreviewData.kt
internal object CollectionPreviewData {

    val collection = CollectionDomain(
        id = "1",
        name = "Italian Classics",
        recipeCount = 12,
        firstRecipeImageUrls = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )
    )

    val collections = List(5) { index ->
        collection.copy(id = index.toString(), name = "Collection $index")
    }
}
```

---

## What to Preview

| Target | Preview? | Notes |
|---|---|---|
| `{Name}Page` | ✅ Always | Primary preview target — full screen with Scaffold |
| `{Name}Content` | ✅ Always | Preview body in isolation for all states |
| Complex components | ✅ When complex | Cards, list items with multiple fields |
| `{Name}Root` | ❌ Never | Cannot be previewed (requires ViewModel, lifecycle) |
| Simple atomic components | ⚠️ Optional | Only if they have significant visual states |

---

## Preview Visibility

All preview composables are `private`. They are not part of the public API.

```kotlin
@ChefMatePreview
@Composable
private fun CollectionPageSuccessPreview() { ... }

@ChefMatePreview
@Composable
private fun CollectionPageLoadingPreview() { ... }
```

---

## Navigator in Previews

Instantiate `{Name}NavigatorImpl()` with no arguments (null controller) for previews.
This is why `NavController` is nullable in `NavigatorImpl`.

```kotlin
CollectionPage(
    state = CollectionState(),
    onAction = {},
    navigator = CollectionNavigatorImpl() // null controller — safe for preview
)
```

---

## Forbidden in Preview Files

- `@Preview` without `ChefMateTheme` wrapper
- Calling `koinViewModel()` or any DI in a preview
- Network image URLs that depend on real data (use placeholder URLs or `null`)
- Complex logic inside the preview composable body
- `@Preview` alone — always use `@PreviewLightDark` minimum
