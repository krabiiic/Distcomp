package com.example.publisher.mapper;

import com.example.publisher.api.dto.request.IssueRequestTo;
import com.example.publisher.api.dto.responce.IssueResponseTo;
import com.example.publisher.model.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IssueMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    Issue fromRequestToEntity(IssueRequestTo request);

    @Mapping(target = "userId", source = "user.id")
    IssueResponseTo fromEntityToResponse(Issue entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Issue entity, IssueRequestTo request);
}
