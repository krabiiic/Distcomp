package com.example.JPA2.Mappers;
import com.example.JPA2.Impl.DTO.MarkRequestTo;
import com.example.JPA2.Impl.DTO.MarkResponseTo;
import com.example.JPA2.Impl.Entity.Mark;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = MarkMapper.class)
public interface MarkListMapper {
    List<Mark> toMarkList(List<MarkRequestTo> list);
    List<MarkResponseTo> toMarkResponseList(List<Mark> list);
}
