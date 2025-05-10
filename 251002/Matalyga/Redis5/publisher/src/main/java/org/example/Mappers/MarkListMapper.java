package org.example.Mappers;
import org.example.Impl.DTO.MarkRequestTo;
import org.example.Impl.DTO.MarkResponseTo;
import org.example.Impl.Entity.Mark;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = MarkMapper.class)
public interface MarkListMapper {
    List<Mark> toMarkList(List<MarkRequestTo> list);
    List<MarkResponseTo> toMarkResponseList(List<Mark> list);
}
