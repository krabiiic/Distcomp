package by.kopvzakone.distcomp.controllers;


import by.kopvzakone.distcomp.dto.ReactionRequestTo;
import by.kopvzakone.distcomp.dto.ReactionResponseTo;
import by.kopvzakone.distcomp.services.ReactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@AllArgsConstructor
public class ReactionKafkaController {
    private final ReactionService serviceImpl;
    public List<ReactionResponseTo> getAll(){
        return serviceImpl.getAll();
    }
    public ReactionResponseTo getById(Long id){
        return serviceImpl.getById(id);
    }
    public ReactionResponseTo create(ReactionRequestTo request){
        return serviceImpl.create(request);
    }
    public void delete(Long id){
        serviceImpl.delete(id);
    }
    public ReactionResponseTo update(ReactionRequestTo request){
        return serviceImpl.update(request);
    }
}
