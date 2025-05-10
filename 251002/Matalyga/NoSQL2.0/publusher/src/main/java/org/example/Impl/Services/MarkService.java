package org.example.Impl.Services;

import org.example.Exceptions.NotFoundException;
import org.example.Exceptions.Violation;
import org.example.Impl.DTO.MarkRequestTo;
import org.example.Impl.DTO.MarkResponseTo;
import org.example.Impl.Entity.Mark;
import org.example.Impl.Repository.MarkRepository;
import org.example.Mappers.MarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkService implements Service<MarkResponseTo, MarkRequestTo>{

    @Autowired
    private MarkMapper markMapper;
    @Autowired
    private MarkRepository markRepository;

    public MarkService() {}

    public List<MarkResponseTo> getAll(){
        return markRepository.getAll()
                .map(markMapper::MarkToMarkResponseTo)
                .toList();
    }

    public MarkResponseTo update(MarkRequestTo updatingMark) {
        if(markRepository.existsById(updatingMark.getId())){
            Mark mark = markMapper.MarkRequestToToMark(updatingMark);
            mark.setId(updatingMark.getId());
            return markMapper.MarkToMarkResponseTo(markRepository.save(mark));
        }
        throw new NotFoundException("No marks found", 404);
    }

    public MarkResponseTo get(long id){
        return markRepository.get(id)
                .map(markMapper::MarkToMarkResponseTo)
                .orElse(null);
    }

    public MarkResponseTo delete(long id) {
        MarkResponseTo markToDelete = get(id);
        if (markRepository.existsById(id)) {
            markRepository.deleteById(id);
        }
        return markToDelete;
    }

    public MarkResponseTo add(MarkRequestTo newMark) {
        try{
            return markRepository.add((markMapper.MarkRequestToToMark(newMark)))
                    .map(markMapper::MarkToMarkResponseTo)
                    .orElseThrow();
        }catch (DataIntegrityViolationException e) {
            throw new Violation("Mark name is already taken");
        }

    }

}
