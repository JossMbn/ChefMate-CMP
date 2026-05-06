---
name: supabase-query
description: >-
  Patterns and examples for writing Supabase queries in ChefMate using supabase-kt 3.6.0.
  Use when asked to "write a supabase query", "fetch from supabase", "insert/update/delete
  in supabase", "call an rpc function", "upload to storage", "call an edge function",
  or any Supabase data operation.
---

# Skill: Supabase Query Patterns

All examples use supabase-kt 3.6.0 with the BOM. All calls go through `safeExecution {}`.

## Golden Rules

1. **Always** use `safeExecution {}` — never raw try/catch in a DataSource
2. **Prefer** `postgrest.rpc()` for complex queries
3. **Always** use RPC function name enums — never inline strings
4. All parameters passed to RPC are `@Serializable` data classes with `@SerialName`
5. Never use `supabase.realtime` — it is not in this project

---

## Pattern 1 — RPC Function Call (preferred for complex queries)

```kotlin
// Parameter class
@Serializable
data class GetCollectionDetailsParameter(
    @SerialName("p_collection_id")
    val collectionId: String,
    @SerialName("p_limit")
    val limit: Int,
    @SerialName("p_offset")
    val offset: Int
)

// In DataSourceImpl
override suspend fun getCollectionDetails(
    collectionId: String,
    page: Int
): Result<CollectionDetailsDomain> {
    val offset = page * PAGINATION_LIMIT
    return supabaseClient.safeExecution {
        postgrest.rpc(
            function = CollectionRpcFunction.GetCollectionDetails.functionName,
            parameters = GetCollectionDetailsParameter(
                collectionId = collectionId,
                limit = PAGINATION_LIMIT,
                offset = offset
            )
        ).decodeAndMap(mapper = CollectionDetailsMapper())
    }
}
```

## Pattern 2 — Decode Extensions

Use the existing decode extensions (declared in `core/network/`):

```kotlin
// Decode single object + map to domain
.decodeAndMap(mapper = CollectionMapper())

// Decode list + map each element to domain
.decodeListAndMap(mapper = CollectionMapper())

// Decode raw (when no mapper needed)
.decodeSingle<CollectionDto>()
.decodeList<CollectionDto>()
```

## Pattern 3 — Direct Table Access (simple CRUD only)

Use only when no RPC function exists and the query is trivial.

```kotlin
// SELECT with filter
override suspend fun getCollectionById(id: String): Result<CollectionDomain> {
    return supabaseClient.safeExecution {
        postgrest.from(CollectionTable.Collections.tableName)
            .select {
                filter {
                    eq(CollectionTableColumn.Id.columnName, id)
                }
            }
            .decodeSingle<CollectionDto>()
            .let { CollectionMapper().convert(it) }
    }
}

// UPDATE
override suspend fun updateCollection(
    collectionId: String,
    newName: String
): Result<CollectionDomain> {
    return supabaseClient.safeExecution {
        postgrest.from(CollectionTable.Collections.tableName)
            .update(
                value = UpdateCollectionParameter(collectionName = newName)
            ) {
                filter {
                    eq(CollectionTableColumn.Id.columnName, collectionId)
                }
            }
            .decodeAndMap(mapper = CollectionMapper())
    }
}
```

## Pattern 4 — Void / Unit Results

For mutations that return nothing:

```kotlin
override suspend fun deleteCollection(id: String): Result<Unit> {
    return supabaseClient.safeExecution {
        postgrest.rpc(
            function = CollectionRpcFunction.DeleteCollection.functionName,
            parameters = DeleteCollectionParameter(collectionId = id)
        )
        // No decode call — safeExecution returns Result<Unit> automatically
    }
}
```

## Pattern 5 — Supabase Auth

```kotlin
// Sign in with email + password
override suspend fun signIn(email: String, password: String): Result<Unit> {
    return supabaseClient.safeExecution {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }
}

// Sign up
override suspend fun signUp(email: String, password: String): Result<Unit> {
    return supabaseClient.safeExecution {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }
}

// Sign out
override suspend fun signOut(): Result<Unit> {
    return supabaseClient.safeExecution {
        auth.signOut()
    }
}

// Get current user (non-suspending)
fun getCurrentUser(): UserInfo? = supabaseClient.auth.currentUserOrNull()

// Observe auth state changes
fun observeAuthState(): Flow<AuthState> = supabaseClient.auth.sessionStatus
    .map { status ->
        when (status) {
            is SessionStatus.Authenticated -> AuthState.Authenticated
            is SessionStatus.NotAuthenticated -> AuthState.NotAuthenticated
            else -> AuthState.Loading
        }
    }
```

## Pattern 6 — Supabase Storage (recipe images)

```kotlin
// Upload image bytes to Storage
override suspend fun uploadRecipeImage(
    recipeId: String,
    imageBytes: ByteArray,
    mimeType: String
): Result<String> {
    val path = "recipes/$recipeId/${System.currentTimeMillis()}.jpg"
    return supabaseClient.safeExecution {
        storage.from(StorageBucket.RecipeImages.bucketName)
            .upload(
                path = path,
                data = imageBytes,
                options = UploadData(
                    contentType = ContentType(mimeType),
                    upsert = false
                )
            )
        // Return the public URL
        storage.from(StorageBucket.RecipeImages.bucketName).publicUrl(path)
    }
}

// Delete image
override suspend fun deleteRecipeImage(imagePath: String): Result<Unit> {
    return supabaseClient.safeExecution {
        storage.from(StorageBucket.RecipeImages.bucketName)
            .delete(paths = listOf(imagePath))
    }
}

enum class StorageBucket(val bucketName: String) {
    RecipeImages("recipe-images")
}
```

## Pattern 7 — Supabase Edge Function (recipe extraction)

```kotlin
// Parameters sent to the Edge Function
@Serializable
data class ExtractRecipeRequest(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("page_url")
    val pageUrl: String? = null,
    @SerialName("raw_text")
    val rawText: String? = null
)

// Response from the Edge Function
@Serializable
data class ExtractRecipeResponse(
    val title: String,
    val description: String? = null,
    val ingredients: List<ExtractedIngredientDto>,
    val steps: List<ExtractedStepDto>,
    @SerialName("source_url")
    val sourceUrl: String? = null
)

// DataSource implementation
override suspend fun extractRecipeFromImage(imageUrl: String): Result<RecipeDomain> {
    return supabaseClient.safeExecution {
        functions.invoke(
            function = EdgeFunction.ExtractRecipe.functionName,
            body = ExtractRecipeRequest(imageUrl = imageUrl)
        ).decodeAndMap(mapper = ExtractedRecipeMapper())
    }
}

enum class EdgeFunction(val functionName: String) {
    ExtractRecipe("extract-recipe")
}
```

## Pattern 8 — Pagination

Always use RPC functions for paginated queries. Pass `limit` and `offset` via parameters.

```kotlin
companion object {
    const val PAGE_SIZE = 20
}

override suspend fun getRecipes(page: Int): Result<List<RecipeDomain>> {
    val offset = page * PAGE_SIZE
    return supabaseClient.safeExecution {
        postgrest.rpc(
            function = RecipeRpcFunction.GetRecipes.functionName,
            parameters = GetRecipesParameter(limit = PAGE_SIZE, offset = offset)
        ).decodeListAndMap(mapper = RecipeMapper())
    }
}
```

---

## Naming Conventions

```kotlin
// RPC function enums — one per feature
enum class CollectionRpcFunction(val functionName: String) {
    GetCollections("get_collections"),
    GetCollectionDetails("get_collection_details"),
    CreateCollection("create_collection"),
    UpdateCollection("update_collection"),
    DeleteCollection("delete_collection"),
    MoveRecipeToCollections("move_recipe_to_collections"),
    GetRecipesByCollectionId("get_recipes_by_collection_id")
}

// Table name enums
enum class CollectionTable(val tableName: String) {
    Collections("collections")
}

// Column name enums
enum class CollectionTableColumn(val columnName: String) {
    Id("id"),
    UserId("user_id"),
    Name("name"),
    CreatedAt("created_at")
}
```

---

## Error Handling Reference

`safeExecution` automatically maps these exceptions:

| Exception | Mapped Error |
|---|---|
| `CancellationException` | Re-thrown (never caught) |
| `RestException("invalid_credentials")` | `AuthenticationError.InvalidCredentials` |
| `RestException("user_already_exists")` | `AuthenticationError.UserAlreadyExists` |
| `RestException` with status 401 | `NetworkError.Unauthorized` |
| `RestException` with status 404 | `NetworkError.NotFound` |
| `RestException` with status 5xx | `NetworkError.ServerError` |
| `HttpRequestTimeoutException` | `NetworkError.TimeoutError` |
| `HttpRequestException` | `NetworkError.NetworkConnectionError` |
| `SerializationException` | `NetworkError.SerializationError` |

Handle in ViewModel with:
```kotlin
.onSuccess { data -> _state.update { it.copy(data = data, isLoading = false) } }
.onFailure { error ->
    val message = when (error) {
        is NetworkError.NetworkConnectionError -> "No internet connection"
        is NetworkError.TimeoutError -> "Request timed out"
        is AuthenticationError.InvalidCredentials -> "Invalid email or password"
        else -> "Something went wrong"
    }
    _state.update { it.copy(error = message, isLoading = false) }
}
```
