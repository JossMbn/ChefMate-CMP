package com.jmabilon.chefmate.core.data.extension

import com.jmabilon.chefmate.core.domain.Mapper
import io.github.jan.supabase.postgrest.result.PostgrestResult

/**
 * Extension functions for [PostgrestResult] to decode and map results using a [Mapper].
 */
inline fun <reified DTO : Any, Domain : Any> PostgrestResult.decodeAndMap(
    mapper: Mapper<Domain, DTO>
): Domain {
    val dto: DTO = this.decodeAs<DTO>()
    return mapper.convert(dto)
}

/**
 * Extension function to decode a list of DTOs from [PostgrestResult] and map them to a list of domain models using a [Mapper].
 */
inline fun <reified DTO : Any, Domain : Any> PostgrestResult.decodeListAndMap(
    mapper: Mapper<Domain, DTO>
): List<Domain> {
    val dtos: List<DTO> = this.decodeList<DTO>()
    return mapper.convert(dtos)
}
