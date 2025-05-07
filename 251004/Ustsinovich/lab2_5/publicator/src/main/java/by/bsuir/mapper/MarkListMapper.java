package by.bsuir.mapper;

import by.bsuir.dto.MarkRequestTo;
import by.bsuir.dto.MarkResponseTo;
import by.bsuir.entities.Mark;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = MarkMapper.class)
public interface MarkListMapper {
    List<Mark> toMarkList(List<MarkRequestTo> marks);
    List<MarkResponseTo> toMarkResponseList(List<Mark> marks);
}
