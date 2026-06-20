package com.jmabilon.chefmate.data.recipe.remote.model

enum class RecipeRpcFunction(val functionName: String) {
    GetRecipeById("get_recipe_by_id"),
    CreateRecipe("create_recipe_with_details"),
    UpdateRecipe("update_recipe_with_details"),
}
