package by.bsuir.utils;

import by.bsuir.exceptions.*;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> catchValidationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(40001L, "Bad request!"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> catchNotFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<ErrorMessage> catchUpdateException(UpdateException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getStatus(), exception.getMessage()));
    }
    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<ErrorMessage> catchDeleteException(DeleteException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorMessage> catchDuplicationException(DuplicationException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorMessage> catchNumberInsteadOfStringExceptionException(JsonParseException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(40006L, exception.getMessage()));
    }
}
