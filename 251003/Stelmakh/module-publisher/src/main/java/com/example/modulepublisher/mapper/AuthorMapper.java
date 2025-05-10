package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.AuthorDTO;
import com.example.modulepublisher.entity.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toAuthor(AuthorDTO authorDTO);
    AuthorDTO toAuthorDTO(Author author);
    List<Author> toAuthorList(List<AuthorDTO> authorDTOList);
    List<AuthorDTO> toAuthorDTOList(List<Author> authors);
}
