package by.kardychka.Publisher.utils.mappers;

import by.kardychka.Publisher.DTOs.Requests.StickerRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.StickerResponseDTO;
import by.kardychka.Publisher.models.Sticker;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StickersMapper {
    StickerResponseDTO toStickerResponse(Sticker sticker);
    List<StickerResponseDTO> toStickerResponseList(List<Sticker> stickers);
    Sticker toSticker(StickerRequestDTO stickerRequestDTO);
    List<Sticker> toStickerList(List<StickerRequestDTO> stickerRequestDTOList);
}
