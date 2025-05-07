package by.andrewbesedin.distcomp.services;

import by.andrewbesedin.distcomp.dto.TagMapper;
import by.andrewbesedin.distcomp.dto.TagRequestTo;
import by.andrewbesedin.distcomp.dto.TagResponseTo;
import by.andrewbesedin.distcomp.dto.*;
import by.andrewbesedin.distcomp.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
    public final TagRepository repImpl;
    @Qualifier("tagMapper")
    public final TagMapper mapper;


    public List<TagResponseTo> getAll() {
        return repImpl.getAll().map(mapper::out).toList();
    }
    @Cacheable(value = "tags", key = "#id")
    public TagResponseTo getById(Long id) {
        return repImpl.get(id).map(mapper::out).orElseThrow();
    }
    @CachePut(value = "tags", key = "#req.id")
    public TagResponseTo create(TagRequestTo req) {
        return repImpl.create(mapper.in(req)).map(mapper::out).orElseThrow();
    }
    @CachePut(value = "tags", key = "#req.id")
    public TagResponseTo update(TagRequestTo req) {
        return repImpl.update(mapper.in(req)).map(mapper::out).orElseThrow();
    }
    @CacheEvict(value = "tags", key = "#id")
    public void delete(Long id) {
        repImpl.delete(id);
    }
}
