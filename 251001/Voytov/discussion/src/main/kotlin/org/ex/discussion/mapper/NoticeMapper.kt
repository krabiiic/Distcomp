package org.ex.discussion.mapper

import org.ex.discussion.dto.request.NoticeRequestDTO
import org.ex.discussion.dto.response.NoticeResponseDTO
import org.ex.discussion.model.Notice
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface NoticeMapper {

    fun toDto(entity: Notice): NoticeResponseDTO

    fun toEntity(dto: NoticeRequestDTO): Notice
}