package by.molchan.Discussion.controllers;


import by.molchan.Discussion.DTOs.Requests.CommentRequestDTO;
import by.molchan.Discussion.DTOs.Responses.CommentResponseDTO;
import by.molchan.Discussion.services.CommentsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final CommentsService commentsService;

    @Autowired
    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDTO> getAllComments() {
        return commentsService.findAll();
    }

    @GetMapping("/{id}")
    public CommentResponseDTO getCommentById(@PathVariable Long id) {
        try {
            return commentsService.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDTO updateComment(@RequestBody @Valid CommentRequestDTO commentRequestDTO) {
        return commentsService.update(commentRequestDTO);
    }

}
