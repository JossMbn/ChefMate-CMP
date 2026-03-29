# ChefMate — Agent Instructions

This file provides essential context for any AI agent working on this repository.
Full coding standards and architecture details are in `.github/copilot-instructions.md`.

## What is ChefMate

ChefMate is a cross-platform mobile recipe management app (Android & iOS) built with
Kotlin Multiplatform and Compose Multiplatform. Users can:
- Create and manage recipes
- Scan photos, blogs, and social media posts to extract recipes via AI
- Organise recipes into Collections

## Project Structure

```
composeApp/
├── androidMain/         # Android entry point (MainActivity, Application)
├── iosMain/             # iOS entry point
└── commonMain/          # All shared code (UI, domain, data)
    └── kotlin/com/chefmate/
        ├── core/        # Shared utilities, theme, navigation host, DI root
        └── feature/     # One package per feature (auth, home, collection, recipe…)
            └── {feature}/
                ├── domain/          # Models, Repository interfaces, UseCases
                ├── data/            # DTOs, Mappers, DataSources, RepositoryImpl
                └── presentation/
                    ├── navigation/  # Route, Navigator, NavGraphBuilder extension
                    ├── screen/      # Root, Page, Content composables + ViewModel
                    └── preview/     # @Composable previews only
```

## Tech Stack

| Concern | Library |
|---|---|
| UI | Compose Multiplatform 1.10.3 + Material 3 |
| Architecture | Clean Architecture + MVI + UDF |
| DI | Koin 4.2.1 |
| Networking / Backend | Supabase-kt 3.6.0 (postgrest, auth, storage, functions) |
| HTTP Engine | Ktor 3.4.3 (OkHttp on Android, Darwin on iOS) |
| Serialization | kotlinx.serialization 1.11.0 |
| Images | Coil 3.4.0 |
| Navigation | Compose Navigation 2.9.2 (JetBrains) |
| Config | BuildKonfig 0.19.0 |
| Kotlin | 2.3.21 — AGP 9.0.1 |

## Key Rules (non-negotiable)

1. Always use `collectAsStateWithLifecycle()` — never `collectAsState()`
2. No business logic inside Composables — ViewModels only
3. No hardcoded API keys or URLs — use BuildKonfig
4. Never call an AI API directly from the client — use Supabase Edge Functions
5. All Supabase calls go through `safeExecution {}` — no bare try/catch in DataSources
6. One UseCase per interface + implementation pair
7. DTOs live in `data/`, Domain models live in `domain/` — no cross-contamination
8. `suspend fun` by default in repositories; `Flow` only when sharing reactive state across ViewModels via an `InMemoryDataSource`
