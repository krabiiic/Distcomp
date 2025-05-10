package com.publisher.rvlab4_publisher.controller;

import com.publisher.rvlab4_publisher.dto.AuthorRequestTo;
import com.publisher.rvlab4_publisher.dto.AuthorResponseTo;
import com.publisher.rvlab4_publisher.exception.CustomException;
import com.publisher.rvlab4_publisher.service.AuthorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthorController(AuthorService authorService, RedisTemplate<String, Object> redisTemplate) {
        this.authorService = authorService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionHandler(ResponseStatusException.class)
    public AuthorResponseTo createAuthor(@RequestBody AuthorRequestTo authorRequestTo) {
        try {
            if (!validateAuthorData(authorRequestTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{}");
            }

            AuthorResponseTo createdAuthor = authorService.createAuthor(authorRequestTo);

            String redisKey = "author:" + createdAuthor.getId();
            redisTemplate.opsForValue().set(redisKey, createdAuthor);

            return createdAuthor;
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "{}");
        }
    }

    private boolean validateAuthorData(AuthorRequestTo authorRequestTo) {
        if (authorRequestTo.getLogin().length() < 2 ||
                authorRequestTo.getLogin().length() > 64) {
            return false;
        }
        if (authorRequestTo.getFirstname().length() < 2 ||
                authorRequestTo.getFirstname().length() > 64) {
            return false;
        }
        if (authorRequestTo.getLastname().length() < 2 ||
                authorRequestTo.getLastname().length() > 64) {
            return false;
        }
        if (authorRequestTo.getPassword().length() < 8 ||
                authorRequestTo.getPassword().length() > 128) {
            return false;
        }
        return true;
    }

    @GetMapping
    public List<AuthorResponseTo> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorResponseTo getAuthorById(@PathVariable Long id) {
        String redisKey = "author:" + id;
        AuthorResponseTo cachedAuthor = (AuthorResponseTo) redisTemplate.opsForValue().get(redisKey);

        if (cachedAuthor != null) {
            return cachedAuthor;
        }

        try {
            AuthorResponseTo author = authorService.getAuthorById(id);
            redisTemplate.opsForValue().set(redisKey, author);
            return author;
        } catch (CustomException ex) {
            throw new CustomException("40002", "Author not found");
        }
    }

    @PutMapping
    public ResponseEntity<AuthorResponseTo> updateAuthor(@RequestBody AuthorRequestTo authorRequestTo) {
        System.out.println("Received PUT request with ID: " + authorRequestTo.getId());
        try {
            if (authorRequestTo.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthorResponseTo());
            }

            AuthorResponseTo updatedAuthor = authorService.updateAuthor(authorRequestTo.getId(), authorRequestTo);

            String redisKey = "author:" + updatedAuthor.getId();
            redisTemplate.opsForValue().set(redisKey, updatedAuthor);

            return ResponseEntity.ok(updatedAuthor);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthorResponseTo());
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthorResponseTo());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthorResponseTo());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            String redisKey = "author:" + id;
            redisTemplate.delete(redisKey);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}