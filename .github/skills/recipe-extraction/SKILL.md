---
name: recipe-extraction
description: >-
  Architecture and implementation guide for ChefMate's recipe extraction feature.
  Use when asked to "extract a recipe", "scan a recipe", "import from photo", "import from
  blog", "import from social media", "analyse an image", "extract from URL", or any task
  related to extracting recipe data from an external source using AI.
---

# Skill: Recipe Extraction

ChefMate extracts recipes from three source types:
- **Photo** (camera or gallery) — image bytes sent to Supabase Storage, then Edge Function
- **URL** (blog or web page) — URL passed directly to Edge Function
- **Text/Post** (social media copy-paste) — raw text passed to Edge Function

**Critical rule**: The AI model is called exclusively from a **Supabase Edge Function**.
Never call OpenAI, Anthropic, Gemini, or any AI API directly from the Kotlin client.

---

## Architecture Overview

```
UI (camera/gallery picker or URL input)
    ↓
ExtractionViewModel.onAction(ExtractionAction.OnExtract*)
    ↓
ExtractRecipeUseCase.invoke(source: ExtractionSource)
    ↓
RecipeExtractionRepository
    ├── [if photo] uploadImageUseCase → Supabase Storage → get imageUrl
    └── extractRecipeRemoteDataSource.extract(source)
            ↓
        supabase.functions.invoke("extract-recipe", body = request)
            ↓
        Edge Function (Deno/TypeScript)
            ├── calls AI model (OpenAI / Anthropic / Gemini)
            └── returns structured JSON
            ↓
        ExtractedRecipeMapper → RecipeDomain (draft, not saved yet)
    ↓
ExtractionViewModel receives Result<RecipeDomain>
    → navigates to RecipeReview screen with draft recipe
```

---

## Domain Layer

### Extraction Source (sealed class)

```kotlin
// feature/extraction/domain/model/ExtractionSource.kt
sealed interface ExtractionSource {
    data class Photo(val imageBytes: ByteArray, val mimeType: String) : ExtractionSource
    data class Url(val url: String) : ExtractionSource
    data class Text(val rawText: String) : ExtractionSource
}
```

### Repository Interface

```kotlin
// feature/extraction/domain/repository/RecipeExtractionRepository.kt
interface RecipeExtractionRepository {
    suspend fun extractRecipe(source: ExtractionSource): Result<RecipeDomain>
}
```

### UseCase

```kotlin
// feature/extraction/domain/usecase/ExtractRecipeUseCase.kt
interface ExtractRecipeUseCase {
    suspend operator fun invoke(source: ExtractionSource): Result<RecipeDomain>
}

class ExtractRecipeUseCaseImpl(
    private val repository: RecipeExtractionRepository
) : ExtractRecipeUseCase {
    override suspend fun invoke(source: ExtractionSource): Result<RecipeDomain> {
        return when (source) {
            is ExtractionSource.Url -> {
                if (source.url.isBlank()) {
                    Result.failure(IllegalArgumentException("URL cannot be empty"))
                } else {
                    repository.extractRecipe(source)
                }
            }
            is ExtractionSource.Text -> {
                if (source.rawText.length < 20) {
                    Result.failure(IllegalArgumentException("Text is too short to extract a recipe"))
                } else {
                    repository.extractRecipe(source)
                }
            }
            is ExtractionSource.Photo -> repository.extractRecipe(source)
        }
    }
}
```

---

## Data Layer

### Request/Response DTOs

```kotlin
// Edge Function request
@Serializable
data class ExtractRecipeRequest(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("page_url")
    val pageUrl: String? = null,
    @SerialName("raw_text")
    val rawText: String? = null
)

// Edge Function response
@Serializable
data class ExtractedRecipeDto(
    val title: String,
    val description: String? = null,
    @SerialName("prep_time_minutes")
    val prepTimeMinutes: Int? = null,
    @SerialName("cook_time_minutes")
    val cookTimeMinutes: Int? = null,
    val servings: Int? = null,
    val ingredients: List<ExtractedIngredientDto> = emptyList(),
    val steps: List<ExtractedStepDto> = emptyList(),
    @SerialName("source_url")
    val sourceUrl: String? = null,
    val language: String? = null
)

@Serializable
data class ExtractedIngredientDto(
    val name: String,
    val quantity: String? = null,
    val unit: String? = null,
    val notes: String? = null
)

@Serializable
data class ExtractedStepDto(
    val order: Int,
    val instruction: String
)
```

### Mapper

```kotlin
class ExtractedRecipeMapper : Mapper<RecipeDomain, ExtractedRecipeDto> {
    override fun convert(input: ExtractedRecipeDto): RecipeDomain {
        return RecipeDomain(
            id = "", // draft — no ID until saved
            title = input.title,
            description = input.description,
            imageUrl = null,
            ingredients = input.ingredients.map { it.toDomain() },
            steps = input.steps.map { it.toDomain() },
            collectionIds = emptyList(),
            createdAt = ""
        )
    }
}
```

### Remote DataSource

```kotlin
class RecipeExtractionRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient,
    private val storageDataSource: RecipeStorageDataSource
) : RecipeExtractionRemoteDataSource {

    override suspend fun extractRecipe(source: ExtractionSource): Result<RecipeDomain> {
        // Step 1 (photo only): upload image and get URL
        val resolvedRequest = when (source) {
            is ExtractionSource.Photo -> {
                storageDataSource.uploadRecipeImage(
                    imageBytes = source.imageBytes,
                    mimeType = source.mimeType
                ).fold(
                    onSuccess = { url ->
                        ExtractRecipeRequest(imageUrl = url)
                    },
                    onFailure = { return Result.failure(it) }
                )
            }
            is ExtractionSource.Url -> ExtractRecipeRequest(pageUrl = source.url)
            is ExtractionSource.Text -> ExtractRecipeRequest(rawText = source.rawText)
        }

        // Step 2: call Edge Function
        return supabaseClient.safeExecution {
            functions.invoke(
                function = EdgeFunction.ExtractRecipe.functionName,
                body = resolvedRequest
            ).decodeAndMap(mapper = ExtractedRecipeMapper())
        }
    }
}
```

---

## Presentation Layer

### State / Action / Event

```kotlin
data class ExtractionState(
    val isLoading: Boolean = false,
    val loadingMessage: String? = null, // "Uploading image…", "Extracting recipe…"
    val error: String? = null,
    val selectedImageBytes: ByteArray? = null,
    val urlInput: String = ""
)

sealed interface ExtractionAction {
    data class OnPhotoSelected(val imageBytes: ByteArray, val mimeType: String) : ExtractionAction
    data class OnUrlChanged(val url: String) : ExtractionAction
    data class OnTextChanged(val text: String) : ExtractionAction
    data object OnExtractFromPhotoClick : ExtractionAction
    data object OnExtractFromUrlClick : ExtractionAction
    data object OnExtractFromTextClick : ExtractionAction
    data object OnRetryClick : ExtractionAction
}

sealed interface ExtractionEvent {
    data class NavigateToReview(val recipe: RecipeDomain) : ExtractionEvent
}
```

### ViewModel

```kotlin
class ExtractionViewModel(
    private val extractRecipeUseCase: ExtractRecipeUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<ExtractionEvent>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(ExtractionState())
    val state = _state.asStateFlow()

    fun onAction(action: ExtractionAction) {
        when (action) {
            is ExtractionAction.OnPhotoSelected ->
                _state.update { it.copy(selectedImageBytes = action.imageBytes) }

            is ExtractionAction.OnUrlChanged ->
                _state.update { it.copy(urlInput = action.url) }

            ExtractionAction.OnExtractFromPhotoClick -> {
                val bytes = _state.value.selectedImageBytes ?: return
                extractFrom(ExtractionSource.Photo(bytes, "image/jpeg"))
            }

            ExtractionAction.OnExtractFromUrlClick ->
                extractFrom(ExtractionSource.Url(_state.value.urlInput))

            ExtractionAction.OnRetryClick ->
                _state.update { it.copy(error = null) }

            else -> Unit
        }
    }

    private fun extractFrom(source: ExtractionSource) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            extractRecipeUseCase(source)
                .onSuccess { recipe ->
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(ExtractionEvent.NavigateToReview(recipe))
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to extract recipe"
                        )
                    }
                }
        }
    }
}
```

---

## Edge Function Contract (Supabase — Deno/TypeScript)

The Kotlin client expects the Edge Function named `extract-recipe` to:

**Accept** (POST body):
```json
{
  "image_url": "https://...",   // one of these three
  "page_url": "https://...",
  "raw_text": "..."
}
```

**Return** (JSON):
```json
{
  "title": "Spaghetti Carbonara",
  "description": "A classic Italian pasta dish",
  "prep_time_minutes": 15,
  "cook_time_minutes": 20,
  "servings": 4,
  "ingredients": [
    { "name": "Spaghetti", "quantity": "400", "unit": "g", "notes": null }
  ],
  "steps": [
    { "order": 1, "instruction": "Boil water and cook spaghetti al dente." }
  ],
  "source_url": "https://..."
}
```

**On error**: return HTTP 4xx/5xx — `safeExecution` will catch and map it.

---

## Platform Considerations for Image Picking

Image picking is platform-specific. Use `expect/actual`:

```kotlin
// commonMain
expect class ImagePicker {
    suspend fun pickImageFromGallery(): ByteArray?
    suspend fun captureFromCamera(): ByteArray?
}

// androidMain
actual class ImagePicker(private val context: Context) {
    // Uses ActivityResultContracts.PickVisualMedia
}

// iosMain
actual class ImagePicker {
    // Uses UIImagePickerController or PHPhotoPicker
}
```

Register `ImagePicker` in Koin platform-specific modules.

---

## Checklist for Recipe Extraction Implementation

- [ ] `ExtractionSource` sealed interface covers Photo, URL, and Text
- [ ] Image upload to Supabase Storage before Edge Function call (Photo source)
- [ ] Edge Function called via `supabase.functions.invoke()` — never direct AI API call
- [ ] `ExtractedRecipeDto` matches Edge Function response schema exactly
- [ ] Draft recipe returned as `RecipeDomain` with empty `id` and `collectionIds`
- [ ] ViewModel shows loading message during upload AND during extraction
- [ ] On success, event emitted to navigate to Review screen (not saved yet)
- [ ] User reviews and can edit the extracted recipe before saving
- [ ] `ImagePicker` uses `expect/actual` for platform-specific implementation
- [ ] All Supabase calls go through `safeExecution {}`
