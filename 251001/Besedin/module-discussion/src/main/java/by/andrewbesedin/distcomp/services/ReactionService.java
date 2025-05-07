package by.andrewbesedin.distcomp.services;

import by.andrewbesedin.distcomp.dto.ReactionMapper;
import by.andrewbesedin.distcomp.dto.ReactionRequestTo;
import by.andrewbesedin.distcomp.dto.ReactionResponseTo;
import by.andrewbesedin.distcomp.dto.*;
import by.andrewbesedin.distcomp.entities.Reaction;
import by.andrewbesedin.distcomp.entities.ReactionKey;
import by.andrewbesedin.distcomp.repositories.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
public class ReactionService {
    public final ReactionRepository repImpl;
    @Qualifier("reactionMapper")
    public final ReactionMapper mapper;
    private final WebClient webClient;

    public List<ReactionResponseTo> getAll() {
        return repImpl.getAll().map(mapper::out).toList();
    }
    public ReactionResponseTo getById(Long id) {
        ReactionKey key = new ReactionKey();
        key.setId(id);
        key.setCountry("by");
        return repImpl.get(key).map(mapper::out).orElseThrow();
    }
    public ReactionResponseTo create(ReactionRequestTo req) {
        Reaction reaction = mapper.in(req);
        return repImpl.create(reaction).map(mapper::out).orElseThrow();
    }
    public ReactionResponseTo update(ReactionRequestTo req) {
        Reaction reaction = mapper.in(req);
        return repImpl.update(reaction).map(mapper::out).orElseThrow();
    }
    public void delete(Long id) {
        ReactionKey key = new ReactionKey();
        key.setId(id);
        key.setCountry("by");
        repImpl.delete(key);
    }
}

