package org.ikrotsyuk.bsuir.modulepublisher.mapper;

import org.ikrotsyuk.bsuir.modulepublisher.dto.request.StickerRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.StickerResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.StickerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StickerMapper {
    StickerResponseDTO toDTO(StickerEntity stickerEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    StickerEntity toEntity(StickerRequestDTO stickerRequestDTO);

    List<StickerEntity> toEntityList(List<StickerRequestDTO> stickerRequestDTOList);

    List<StickerResponseDTO> toDTOList(List<StickerEntity> stickerEntityList);
}
