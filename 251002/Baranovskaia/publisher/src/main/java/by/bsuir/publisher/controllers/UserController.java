package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.UserRequestDto;
import by.bsuir.publisher.dto.responses.UserResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto user) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.readAll());
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto user) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.delete(id));
    }
}
