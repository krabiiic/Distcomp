package org.ikrotsyuk.bsuir.modulepublisher.mapper;

import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ArticleRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ArticleResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.ArticleEntity;
import org.ikrotsyuk.bsuir.modulepublisher.entity.WriterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "writerId", expression = "java(articleEntity.getWriter().getId())")
    ArticleResponseDTO toDTO(ArticleEntity articleEntity);

    @Mapping(target = "writer", expression = "java(mapWriterIdToWriter(articleRequestDTO.getWriterId()))")
    ArticleEntity toEntity(ArticleRequestDTO articleRequestDTO);

    default WriterEntity mapWriterIdToWriter(Long writerId) {
        if (writerId == null) {
            return null;
        }
        WriterEntity writer = new WriterEntity();
        writer.setId(writerId);
        return writer;
    }

    List<ArticleResponseDTO> toDTOList(List<ArticleEntity> articleEntityList);
}
