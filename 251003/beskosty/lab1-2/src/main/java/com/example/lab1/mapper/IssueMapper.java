package com.example.lab1.mapper;

import com.example.lab1.dto.IssueRequestTo;
import com.example.lab1.dto.IssueResponseTo;
import com.example.lab1.model.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "modified", expression = "java(java.time.LocalDateTime.now())")
    Issue toEntity(IssueRequestTo dto);

    IssueResponseTo toDto(Issue entity);
}
