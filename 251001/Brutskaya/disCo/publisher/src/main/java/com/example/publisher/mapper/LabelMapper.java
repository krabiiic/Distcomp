package com.example.publisher.mapper;

import com.example.publisher.api.dto.request.LabelRequestTo;
import com.example.publisher.api.dto.responce.LabelResponseTo;
import com.example.publisher.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LabelMapper {

    Label fromRequestToEntity(LabelRequestTo request);

    LabelResponseTo fromEntityToResponse(Label entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Label entity, LabelRequestTo request);
}
