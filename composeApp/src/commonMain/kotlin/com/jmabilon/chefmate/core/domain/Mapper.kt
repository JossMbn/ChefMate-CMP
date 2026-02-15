package com.jmabilon.chefmate.core.domain

/**
 * A generic interface for mapping between two types.
 *
 * @param OutputType The type to which the input will be converted.
 * @param InputType The type that will be converted to the output type.
 */
interface Mapper<OutputType : Any, InputType : Any> {

    fun convert(input: InputType): OutputType

    fun convert(inputs: List<InputType>): List<OutputType> {
        return inputs.map { convert(it) }
    }

    fun convertOrEmpty(input: List<InputType>?): List<OutputType> {
        return convert(input ?: emptyList())
    }
}
