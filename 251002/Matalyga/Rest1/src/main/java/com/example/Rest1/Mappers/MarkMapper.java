package com.example.Rest1.Mappers;

import com.example.Rest1.Impl.Entity.Mark;
import com.example.Rest1.Impl.DTO.MarkRequestTo;
import com.example.Rest1.Impl.DTO.MarkResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarkMapper {
    MarkMapper INSTANCE = Mappers.getMapper(MarkMapper.class);
    Mark MarkResponseToToMark(MarkResponseTo responseTo);
    Mark MarkRequestToToMark(MarkRequestTo requestTo);
    MarkRequestTo MarkToTagRequestMark(Mark mark);
    MarkResponseTo MarkToMarkResponseTo(Mark mark);
}

