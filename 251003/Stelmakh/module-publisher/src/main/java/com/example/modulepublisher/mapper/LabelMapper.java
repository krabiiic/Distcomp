package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.LabelDTO;
import com.example.modulepublisher.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    Label toLabel(LabelDTO labelDTO);
    LabelDTO toLabelDTO(Label label);
    List<LabelDTO> toLabelDTOList(List<Label> labels);
    List<Label> toLabelList(List<LabelDTO> labelDTOS);
}
