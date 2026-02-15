package com.jmabilon.chefmate.data.recipe.source.remote.model

enum class RecipeRpcFunction(val functionName: String) {
    GetRecipeById("get_recipe_with_details"),
    CreateRecipe("create_recipe_with_details"),
    UpdateRecipe("update_recipe_with_details"),
}
