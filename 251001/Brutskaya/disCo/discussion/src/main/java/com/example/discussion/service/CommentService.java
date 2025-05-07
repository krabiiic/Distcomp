package com.example.discussion.service;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.discussion.model.Comment;
import com.example.discussion.model.enums.State;
import com.example.discussion.mupper.CommentMapper;
import com.example.discussion.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final SequenceGeneratorService sequenceGeneratorService;

    public CommentResponseTo create(CommentRequestTo request) {
        Comment comment = commentMapper.fromRequestToEntity(request);
        comment.setId(sequenceGeneratorService.generateSequence(Comment.SEQUENCE_NAME));
        request.setState(State.PENDING);
        return commentMapper.fromEntityToResponse(commentRepository.save(comment));
    }

    public List<CommentResponseTo> getAll(){
        return commentRepository.findAll().stream()
                .map(commentMapper::fromEntityToResponse)
                .toList();
    }

    public CommentResponseTo getById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public CommentResponseTo update(CommentRequestTo request) {
        Comment entity = commentRepository.findById(request.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        commentMapper.updateEntity(entity, request);
        return commentMapper.fromEntityToResponse(commentRepository.save(entity));
    }

    public void delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
