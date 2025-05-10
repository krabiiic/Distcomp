package org.ex.discussion.mapper

import org.ex.discussion.dto.request.ReactionRequestDTO
import org.ex.discussion.dto.response.ReactionResponseDTO
import org.ex.discussion.model.Reaction
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface ReactionMapper {

    fun toDto(entity: Reaction): ReactionResponseDTO

    fun toEntity(dto: ReactionRequestDTO): Reaction
}