package by.bsuir.distcomp.service;

import by.bsuir.distcomp.dto.mapper.AuthorMapper;
import by.bsuir.distcomp.dto.request.AuthorRequestTo;
import by.bsuir.distcomp.dto.response.AuthorResponseTo;
import by.bsuir.distcomp.repository.AuthorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Cacheable(value = "authors", key = "'all'")
    public List<AuthorResponseTo> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Cacheable(value = "authors", key = "#id")
    public AuthorResponseTo getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Author with id: " + id + " not found"));
    }

    @Transactional
    @CacheEvict(value = "authors", key = "'all'")
    public AuthorResponseTo createAuthor(AuthorRequestTo authorRequestTo) {
        return authorMapper.toDto(authorRepository.save(authorMapper.toEntity(authorRequestTo)));
    }

    @Transactional
    @Caching (
            put = @CachePut(value = "authors", key = "#authorRequestTo.id"),
            evict = @CacheEvict(value = "authors", key = "'all'")
    )
    public AuthorResponseTo updateAuthor(AuthorRequestTo authorRequestTo) {
        Long id = authorRequestTo.getId();
        authorRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author with id: " + id + " not found"));

        return authorMapper.toDto(authorRepository.save(authorMapper.toEntity(authorRequestTo)));
    }

    @Transactional
    @Caching (
            evict = {
                    @CacheEvict(value = "authors", key = "#id"),
                    @CacheEvict(value = "authors", key = "'all'")
            }
    )
    public void deleteAuthor(Long id) {
        authorRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author with id: " + id + " not found"));

        authorRepository.deleteById(id);
    }
    
}
