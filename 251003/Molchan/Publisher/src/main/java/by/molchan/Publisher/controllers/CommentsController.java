package by.molchan.Publisher.controllers;

import  by.molchan.Publisher.services.CommentsService;
import by.molchan.Publisher.DTOs.Requests.CommentRequestDTO;
import by.molchan.Publisher.DTOs.Responses.CommentResponseDTO;
import by.molchan.Publisher.utils.CommentValidator;
import by.molchan.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;
    private final CommentValidator commentValidator;
    @Autowired
    public CommentsController(CommentsService commentsService, CommentValidator commentValidator) {
        this.commentsService = commentsService;
        this.commentValidator = commentValidator;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDTO createComment(@RequestBody @Valid CommentRequestDTO commentRequestDTO,
                                      BindingResult bindingResult) {
        validateRequest(commentRequestDTO, bindingResult);
        return commentsService.createComment(commentRequestDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDTO getCommentById(@PathVariable Long id) {
        return commentsService.getCommentById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDTO> getAllComments() {
        return commentsService.getAllComments();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDTO updateComment(
            @RequestBody @Valid CommentRequestDTO commentRequestDTO) {
        return commentsService.processCommentRequest("PUT", commentRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        CommentRequestDTO request = new CommentRequestDTO();
        request.setId(id);
        commentsService.processCommentRequest("DELETE", request);
    }

    private void validateRequest(CommentRequestDTO request, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            commentValidator.validate(request, bindingResult);
        }
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}