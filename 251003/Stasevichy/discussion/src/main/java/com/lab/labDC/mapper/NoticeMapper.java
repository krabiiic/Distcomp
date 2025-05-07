package com.lab.labDC.mapper;

import com.lab.labDC.dto.NoticeRequestTo;
import com.lab.labDC.dto.NoticeResponseTo;
import com.lab.labDC.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoticeMapper {

    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    Notice toEntity(NoticeRequestTo noticeRequestTo);

    NoticeResponseTo toResponseDto(Notice notices);

    Notice toEntity(NoticeResponseTo noticeResponseTo);

    NoticeRequestTo toRequestDto(Notice notices);
}
