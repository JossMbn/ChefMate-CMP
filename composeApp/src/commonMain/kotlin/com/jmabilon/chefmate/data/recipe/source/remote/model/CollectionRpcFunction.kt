package com.jmabilon.chefmate.data.recipe.source.remote.model

enum class CollectionRpcFunction(val functionName: String) {
    GetCollections("get_collections"),
    GetCollectionDetails("get_collection_details"),
    GetRecipesByCollectionId("get_recipes_by_collection"),
    CreateCollection("create_collection"),
    DeleteCollection("delete_collection"),
    MoveRecipeToCollections("move_recipe_to_collections"),
    ToggleRecipeToFavoriteCollection("toggle_recipe_favorite"),
}
