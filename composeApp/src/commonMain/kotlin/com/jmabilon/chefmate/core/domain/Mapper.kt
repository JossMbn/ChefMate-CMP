package com.jmabilon.chefmate.core.domain

import io.github.jan.supabase.postgrest.result.PostgrestResult

interface Mapper<OutputType : Any, InputType : Any> {

    fun convert(input: InputType): OutputType

    fun convert(inputs: List<InputType>): List<OutputType> {
        return inputs.map { convert(it) }
    }

    fun convertOrEmpty(input: List<InputType>?): List<OutputType> {
        return convert(input ?: emptyList())
    }
}

// =================================================================================================
//  Extension functions to decode PostgrestResult and map the decoded DTO
//  to a domain model using the provided Mapper.
// =================================================================================================

suspend inline fun <reified DTO : Any, Domain : Any> PostgrestResult.decodeAndMap(
    mapper: Mapper<Domain, DTO>
): Domain {
    val dto: DTO = this.decodeAs<DTO>()
    return mapper.convert(dto)
}

suspend inline fun <reified DTO : Any, Domain : Any> PostgrestResult.decodeListAndMap(
    mapper: Mapper<Domain, DTO>
): List<Domain> {
    val dtos: List<DTO> = this.decodeList<DTO>()
    return mapper.convert(dtos)
}
