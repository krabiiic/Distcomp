package org.ikrotsyuk.bsuir.modulepublisher.mapper;

import org.ikrotsyuk.bsuir.modulepublisher.dto.request.WriterRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.WriterResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.WriterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WriterMapper {
    WriterResponseDTO toDTO(WriterEntity writerEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    WriterEntity toEntity(WriterRequestDTO writerRequestDTO);

    List<WriterResponseDTO> toDTOList(List<WriterEntity> writerEntityList);
}
