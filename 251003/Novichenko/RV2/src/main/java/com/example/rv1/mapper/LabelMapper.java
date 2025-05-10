package com.example.rv1.mapper;

import com.example.rv1.dto.LabelDTO;
import com.example.rv1.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    Label toLabel(LabelDTO labelDTO);
    LabelDTO toLabelDTO(Label label);
    List<LabelDTO> toLabelDTOList(List<Label> labels);
    List<Label> toLabelList(List<LabelDTO> labelDTOS);
}
