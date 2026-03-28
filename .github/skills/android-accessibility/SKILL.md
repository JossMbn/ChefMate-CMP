---
name: android-accessibility
description: Expert checklist and prompts for auditing and fixing Android accessibility issues, especially in Jetpack Compose.
---

# Android Accessibility Skill

## Overview

This skill ensures all UI components in the ChefMate Compose Multiplatform app are accessible to users with disabilities, following accessibility best practices adapted for shared Compose UI across Android and iOS.

> **Note**: Since ChefMate uses Compose Multiplatform, accessibility semantics set in `commonMain` are mapped to both Android (TalkBack) and iOS (VoiceOver) accessibility services. Always write accessibility code in `commonMain` unless platform-specific behavior is required.

---

## Content Descriptions

- **Always** provide meaningful `contentDescription` for interactive `Icon` and `Image` composables.
- Use `null` for `contentDescription` only when the element is purely decorative **and** the parent already conveys the semantic meaning (e.g., an icon inside a labeled button).
- Use Compose Multiplatform string resources (`org.jetbrains.compose.resources.stringResource`) for content descriptions to support localization.

```kotlin
// ✅ Decorative icon inside a labeled button — contentDescription = null is acceptable
IconButton(onClick = navigator::navigateToAccountPage) {
    Icon(
        painter = painterResource(Res.drawable.ic_person_rounded),
        contentDescription = stringResource(Res.string.cd_account)
    )
}

// ✅ Standalone icon that conveys meaning
Icon(
    painter = painterResource(Res.drawable.ic_add_rounded),
    contentDescription = stringResource(Res.string.cd_add_recipe)
)
```

---

## Semantic Properties

- Use `Modifier.semantics { }` to provide additional context for screen readers when the default composable semantics are insufficient.
- Use `Modifier.clearAndSetSemantics { }` to replace auto-generated semantics on container composables that aggregate child content.
- Group related elements using `Modifier.semantics(mergeDescendants = true) { }` to avoid overly verbose TalkBack announcements.

```kotlin
Row(
    modifier = Modifier.semantics(mergeDescendants = true) { }
) {
    Icon(painter = painterResource(Res.drawable.ic_schedule_rounded), contentDescription = null)
    Text(text = "30 min")
}
```

---

## Touch Targets

- Ensure all interactive composables meet the minimum touch target size of **48dp × 48dp**.
- Use `Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)` when a custom clickable component is smaller than the minimum.
- `IconButton` and `Button` from Material 3 already enforce minimum touch targets by default.

---

## Focus & Traversal Order

- Use `Modifier.focusRequester()` and `Modifier.focusProperties { }` to control focus order when the visual layout doesn't match the logical reading order.
- Ensure modal bottom sheets (`BottomSheetContainer`) trap focus while visible and return focus to the trigger element on dismiss.
- Use `Modifier.onKeyEvent { }` for keyboard navigation support on physical keyboards and external devices.

---

## State Announcements

- Use `LiveRegion` semantics to announce dynamic content changes (e.g., loading states, error messages, snackbar content).

```kotlin
Text(
    text = if (state.isLoading) "Loading…" else "Content loaded",
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Polite
    }
)
```

- Toggle states (e.g., `SegmentedButton` selections) should convey their selected/unselected status via semantics.

---

## Heading Hierarchy

- Mark section titles as headings using `Modifier.semantics { heading() }` to allow screen reader users to navigate by heading.

```kotlin
Text(
    text = "Ingredients",
    style = MaterialTheme.typography.titleMedium,
    modifier = Modifier.semantics { heading() }
)
```

---

## Color & Contrast

- Never rely on color alone to convey information — pair color with text, icons, or shape.
- Use `MaterialTheme.colorScheme` tokens which are designed for sufficient contrast between foreground/background pairs (e.g., `onPrimary` on `primary`).
- Support both light and dark themes via `ChefMateTheme` — test accessibility in both modes.

---

## Error & Validation States

- Use `Modifier.semantics { error("…") }` on text fields with validation errors so screen readers announce the error.
- Pair visual error indicators with text descriptions.

```kotlin
CMTextField(
    value = state.email,
    onValueChange = { onAction(SignInAction.OnEmailValueChange(it)) },
    isError = state.emailError != null,
    modifier = Modifier.semantics {
        if (state.emailError != null) {
            error(state.emailError)
        }
    }
)
```

---

## Testing Checklist

### Android
- Enable **TalkBack** and navigate every screen to verify logical reading order and meaningful announcements.
- Use **Accessibility Scanner** to detect touch target, contrast, and labeling issues.
- Test with **Switch Access** and keyboard-only navigation.

### iOS
- Enable **VoiceOver** and navigate every screen to verify logical reading order and meaningful announcements.
- Test with **Voice Control** and keyboard-only navigation.

### Both platforms
- Verify that `@Preview` composables render correctly with large font sizes (display scaling up to 200%).
- Confirm that all Compose semantics set in `commonMain` are correctly interpreted on both platforms.
