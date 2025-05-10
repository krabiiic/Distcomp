package publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(DataObjectNotFoundException.class)
    public ResponseEntity<Object> notFoundException(DataObjectNotFoundException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenObjectException.class)
    public ResponseEntity<Object> duplicateLoginException(ForbiddenObjectException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalFieldDataException.class)
    public ResponseEntity<Object> userNotFoundException(IllegalFieldDataException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

