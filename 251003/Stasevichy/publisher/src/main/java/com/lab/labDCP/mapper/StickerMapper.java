package com.lab.labDCP.mapper;

import com.lab.labDCP.dto.StickerRequestTo;
import com.lab.labDCP.dto.StickerResponseTo;
import com.lab.labDCP.entity.Sticker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StickerMapper {

    StickerMapper INSTANCE = Mappers.getMapper(StickerMapper.class);

    Sticker toEntity(StickerRequestTo stickerRequestTo);

    StickerResponseTo toResponseDto(Sticker sticker);

    Sticker toEntity(StickerResponseTo stickerResponseTo);

    StickerRequestTo toRequestDto(Sticker sticker);
}
