package com.example.rv1.mapper;

import com.example.rv1.dto.MarkDTO;
import com.example.rv1.entity.Mark;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark toMark(MarkDTO markDTO);
    MarkDTO toMarkDTO(Mark mark);
    List<MarkDTO> toMarkDTOList(List<Mark> marks);
    List<Mark> toMarkList(List<MarkDTO> markDTOS);
}
