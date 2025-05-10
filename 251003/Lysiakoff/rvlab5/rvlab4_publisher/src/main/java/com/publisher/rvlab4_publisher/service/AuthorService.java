package com.publisher.rvlab4_publisher.service;

import com.publisher.rvlab4_publisher.dto.AuthorRequestTo;
import com.publisher.rvlab4_publisher.dto.AuthorResponseTo;
import com.publisher.rvlab4_publisher.entity.Author;
import com.publisher.rvlab4_publisher.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<AuthorResponseTo> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> new AuthorResponseTo(
                        author.getId(),
                        author.getLogin(),
                        author.getFirstname(),
                        author.getLastname(),
                        new ArrayList<>()
                ))
                .collect(Collectors.toList());
    }

    public AuthorResponseTo getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(author -> new AuthorResponseTo(
                        author.getId(),
                        author.getLogin(),
                        author.getFirstname(),
                        author.getLastname(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

    public AuthorResponseTo createAuthor(AuthorRequestTo request) {
        Author author = new Author();
        author.setLogin(request.getLogin());
        author.setPassword(request.getPassword());
        author.setFirstname(request.getFirstname());
        author.setLastname(request.getLastname());

        Author savedAuthor = authorRepository.save(author);

        return new AuthorResponseTo(
                savedAuthor.getId(),
                savedAuthor.getLogin(),
                savedAuthor.getFirstname(),
                savedAuthor.getLastname(),
                new ArrayList<>()
        );
    }

    public AuthorResponseTo updateAuthor(Long id, AuthorRequestTo request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));

        if (request.getLogin() == null || request.getLogin().length() < 3) {
            throw new IllegalArgumentException("Login must be at least 3 characters long");
        }

        author.setLogin(request.getLogin());
        author.setPassword(request.getPassword());
        author.setFirstname(request.getFirstname());
        author.setLastname(request.getLastname());

        Author updatedAuthor = authorRepository.save(author);

        return new AuthorResponseTo(
                updatedAuthor.getId(),
                updatedAuthor.getLogin(),
                updatedAuthor.getFirstname(),
                updatedAuthor.getLastname(),
                new ArrayList<>()
        );
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NoSuchElementException("Author not found");
        }
        authorRepository.deleteById(id);
    }
}
