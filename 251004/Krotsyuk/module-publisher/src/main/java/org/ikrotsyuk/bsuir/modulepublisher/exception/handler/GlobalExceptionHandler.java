package org.ikrotsyuk.bsuir.modulepublisher.exception.handler;

import org.ikrotsyuk.bsuir.modulepublisher.controller.ArticleController;
import org.ikrotsyuk.bsuir.modulepublisher.controller.ReactionController;
import org.ikrotsyuk.bsuir.modulepublisher.controller.StickerController;
import org.ikrotsyuk.bsuir.modulepublisher.controller.WriterController;
import org.ikrotsyuk.bsuir.modulepublisher.exception.dto.ExceptionDTO;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.ArticleWithSameTitleFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.FeignException;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NoArticleWithReactionArticleIdFound;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NoWriterWithArticleWriterIdFound;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.UserWithSameLoginFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = {ArticleController.class, WriterController.class, StickerController.class, ReactionController.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionDTO> handleFeignException(FeignException ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NotFoundException ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithSameLoginFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUserWithSameLoginFoundException(UserWithSameLoginFoundException ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ArticleWithSameTitleFoundException.class)
    public ResponseEntity<ExceptionDTO> handleArticleWithSameTitleFoundException(ArticleWithSameTitleFoundException ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoWriterWithArticleWriterIdFound.class)
    public ResponseEntity<ExceptionDTO> handleNoWriterWithArticleWriterIdFound(NoWriterWithArticleWriterIdFound ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoArticleWithReactionArticleIdFound.class)
    public ResponseEntity<ExceptionDTO> handleNoArticleWithReactionArticleIdFound(NoArticleWithReactionArticleIdFound ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
    }
}
