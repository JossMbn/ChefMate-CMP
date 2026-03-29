---
applyTo: "**/*Content.kt,**/*Component*.kt,**/*Page.kt"
---

# Compose Accessibility Instructions

Based on official Google accessibility guidelines for Jetpack Compose and
Material 3 best practices.

## Core Principle

Every user must be able to use ChefMate with a screen reader (TalkBack on Android,
VoiceOver on iOS). Accessibility is not optional — it is a correctness requirement.

---

## Content Descriptions

### Images — Always Required

Every `AsyncImage`, `Image`, or `Icon` that conveys information **must** have a
non-null, meaningful `contentDescription`.

```kotlin
// ✅ Correct
AsyncImage(
    model = recipe.imageUrl,
    contentDescription = stringResource(
        Res.string.recipe_image_description,
        recipe.name
    )
)

// ✅ Decorative image — explicitly mark as decorative
AsyncImage(
    model = backgroundDecoration,
    contentDescription = null // explicitly null for decorative elements
)

// ❌ Wrong — missing contentDescription
AsyncImage(model = recipe.imageUrl, contentDescription = "")
```

### Icons

```kotlin
// ✅ Correct — icon with action context
IconButton(onClick = { onAction(ExampleAction.OnDeleteClick) }) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = stringResource(Res.string.delete_collection_description)
    )
}

// ✅ Correct — icon that is purely decorative inside a labelled button
Button(onClick = { onAction(ExampleAction.OnSaveClick) }) {
    Icon(
        imageVector = Icons.Default.Save,
        contentDescription = null // button text provides context
    )
    Spacer(Modifier.width(8.dp))
    Text(stringResource(Res.string.save))
}
```

---

## Touch Target Size

Every interactive element must have a minimum touch target of **48.dp × 48.dp**.
This is a Material 3 and Google accessibility requirement.

```kotlin
// ✅ Use minimumInteractiveComponentSize (Material 3 automatic enforcement)
IconButton(onClick = { ... }) { // already 48.dp by default in M3
    Icon(...)
}

// ✅ Manual enforcement when needed
Box(
    modifier = Modifier
        .minimumInteractiveComponentSize()
        .clickable { onClick() }
)
```

Never reduce the size of interactive components below 48.dp even for visual reasons.
Use `padding` to reduce the visual appearance while maintaining the touch target.

---

## Semantic Merging for Composite Elements

When a card or row contains multiple elements that together represent a single action,
merge their semantics so screen readers read them as one item.

```kotlin
// ✅ Correct — card is one semantically merged element
Card(
    modifier = Modifier
        .fillMaxWidth()
        .semantics(mergeDescendants = true) {}
        .clickable { onClick() }
) {
    AsyncImage(
        model = collection.imageUrl,
        contentDescription = null // description handled by merged semantics below
    )
    Text(collection.name)
    Text(
        text = stringResource(Res.string.recipe_count, collection.recipeCount),
    )
}
```

---

## Custom Semantics for Complex Components

When the default semantics are insufficient, provide custom semantic descriptions.

```kotlin
// ✅ Recipe card with rich semantic description
Card(
    modifier = Modifier.semantics {
        contentDescription = context.getString(
            R.string.recipe_card_description,
            recipe.name,
            recipe.collectionCount
        )
        role = Role.Button
    }
) { ... }
```

Use `clearAndSetSemantics {}` to completely replace default semantics when the
default traversal order would be confusing.

```kotlin
// ✅ Replace fragmented semantics with a single meaningful description
Row(
    modifier = Modifier.clearAndSetSemantics {
        contentDescription = "${collection.name}, ${collection.recipeCount} recipes"
        role = Role.Button
        onClick(label = "Open collection") { onAction(...); true }
    }
) { ... }
```

---

## Color Independence

Never use color as the only way to convey information.

```kotlin
// ❌ Wrong — error state communicated only by color
Text(
    text = state.errorMessage,
    color = MaterialTheme.colorScheme.error
)

// ✅ Correct — error state communicated by icon + text + color
Row {
    Icon(
        imageVector = Icons.Default.Error,
        contentDescription = stringResource(Res.string.error_icon_description),
        tint = MaterialTheme.colorScheme.error
    )
    Text(
        text = state.errorMessage,
        color = MaterialTheme.colorScheme.error
    )
}
```

---

## Text Contrast & Scaling

- Use `MaterialTheme.colorScheme` pairs (`onSurface` on `surface`, `onPrimary` on `primary`, etc.)
  — they are guaranteed to meet WCAG AA contrast ratio (4.5:1 minimum).
- Never override text color with a custom value without verifying contrast ratio.
- Never use `sp` fixed sizes for text — always use `MaterialTheme.typography` styles.
- Never disable font scaling (`fontScale = 1f` in `LocalDensity`) — ChefMate must
  support large text for accessibility. Test with `@PreviewFontScale`.

---

## Focus & Keyboard Navigation

For interactive elements in a list or form:

```kotlin
// ✅ Explicit focus order when the visual order differs from the logical order
Row {
    TextField(
        modifier = Modifier.focusRequester(nameFocusRequester),
        ...
    )
    TextField(
        modifier = Modifier.focusRequester(descriptionFocusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { descriptionFocusRequester.requestFocus() }
        ),
        ...
    )
}
```

---

## Loading & Dynamic Content

Announce dynamic content changes to accessibility services.

```kotlin
// ✅ Announce loading state changes
Box(
    modifier = Modifier.semantics {
        if (state.isLoading) {
            liveRegion = LiveRegionMode.Polite
            contentDescription = stringResource(Res.string.loading_collections)
        }
    }
) {
    if (state.isLoading) CircularProgressIndicator()
}
```

---

## Checklist Before Submitting a Screen

- [ ] All images have `contentDescription` (meaningful or explicitly `null` if decorative)
- [ ] All icon-only buttons have `contentDescription` on the `Icon`
- [ ] All touch targets are at least 48.dp × 48.dp
- [ ] Composite clickable elements use `semantics(mergeDescendants = true)`
- [ ] Color is not the only indicator of state (error, success, selection)
- [ ] Text uses `MaterialTheme.typography` — no fixed `sp` values
- [ ] Screen tested with `@PreviewFontScale` annotation
- [ ] No hardcoded `contentDescription` strings — all in string resources
