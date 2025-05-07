package org.ex.distributed_computing.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.AuthorRequestDTO;
import org.ex.distributed_computing.dto.response.AuthorResponseDTO;
import org.ex.distributed_computing.exception.DuplicateDatabaseValueException;
import org.ex.distributed_computing.exception.NotFoundException;
import org.ex.distributed_computing.mapper.AuthorMapper;
import org.ex.distributed_computing.model.Author;
import org.ex.distributed_computing.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  @Qualifier("authorCache")
  private final Cache cache;

  public List<AuthorResponseDTO> getAllAuthors() {
    return authorMapper.toDtoList(authorRepository.findAll());
  }

  public AuthorResponseDTO getAuthorById(Long id) {
    var cached = cache.get(id, AuthorResponseDTO.class);
    if (cached != null) {
      return cached;
    }

    var author = authorRepository.findById(id)
        .map(authorMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Author not found"));

    cache.put(id, author);
    return author;
  }

  public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
    Author author = authorMapper.toEntity(requestDTO);
    if (authorRepository.existsByLogin(author.getLogin())) {
      throw new DuplicateDatabaseValueException();
    }
    authorRepository.save(author);
    var authorDto = authorMapper.toDto(author);
    cache.put(author.getId(), authorDto);
    return authorDto;
  }

  public AuthorResponseDTO updateAuthor(AuthorRequestDTO dto) {
    Author author = authorRepository.findById(dto.id())
        .orElseThrow(() -> new NotFoundException("Author not found"));

    author.setLogin(dto.login());
    author.setPassword(dto.password());
    author.setFirstname(dto.firstname());
    author.setLastname(dto.lastname());

    authorRepository.save(author);
    var authorDto = authorMapper.toDto(author);
    cache.put(author.getId(), authorDto);
    return authorDto;
  }

  public void deleteAuthor(Long id) {
    Author author = authorRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Author not found"));
    authorRepository.delete(author);
    cache.evictIfPresent(id);
  }
}

