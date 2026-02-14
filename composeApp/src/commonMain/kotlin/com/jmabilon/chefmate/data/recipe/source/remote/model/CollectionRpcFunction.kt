package com.jmabilon.chefmate.data.recipe.source.remote.model

enum class CollectionRpcFunction(val functionName: String) {
    GetRecipesByCollectionId("get_recipes_by_collection"),
    DeleteCollection("delete_collection"),
    MoveRecipeToCollections("move_recipe_to_collections")
}
