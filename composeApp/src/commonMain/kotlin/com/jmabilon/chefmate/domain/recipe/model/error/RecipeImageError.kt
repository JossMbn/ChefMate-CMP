package com.jmabilon.chefmate.domain.recipe.model.error

sealed class RecipeImageError : Throwable() {
    class ImageSizeTooLarge : RecipeImageError()
    class InvalidImageData : RecipeImageError()
    class CompressionFailed : RecipeImageError()
}
