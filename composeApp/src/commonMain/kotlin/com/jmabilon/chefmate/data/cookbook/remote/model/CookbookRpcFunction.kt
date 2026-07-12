package com.jmabilon.chefmate.data.cookbook.remote.model

enum class CookbookRpcFunction(val functionName: String) {
    GetCookbooks("get_collections"),
    GetCookbookDetails("get_collection_details"),
    GetRecipesByCookbookId("get_recipes_by_collection"),
    CreateCookbook("create_collection"),
    DeleteCookbook("delete_collection"),
    MoveRecipeToCookbooks("move_recipe_to_collections"),
    ToggleRecipeToFavoriteCookbook("toggle_recipe_favorite"),
}
