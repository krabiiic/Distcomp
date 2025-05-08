package com.dedov.distributed_computing.service;

import com.dedov.distributed_computing.dao.InMemoryMarkDao;
import com.dedov.distributed_computing.dto.request.MarkRequestTo;
import com.dedov.distributed_computing.dto.response.MarkResponseTo;
import com.dedov.distributed_computing.exception.EntityNotFoundException;
import com.dedov.distributed_computing.model.Mark;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarkService {

    private final ModelMapper modelMapper;
    private final InMemoryMarkDao markDao;

    @Autowired
    public MarkService(ModelMapper modelMapper, InMemoryMarkDao markDao) {
        this.modelMapper = modelMapper;
        this.markDao = markDao;
    }

    public List<MarkResponseTo> findAll() {
        return markDao.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MarkResponseTo findById(long id) throws EntityNotFoundException {
        Mark mark = markDao.findById(id).orElseThrow(() -> new EntityNotFoundException("This editor doesn't exist."));

        return convertToResponse(mark);
    }

    public MarkResponseTo save(MarkRequestTo markRequestTo) {
        Mark mark = convertToMark(markRequestTo);
        markDao.save(mark);
        return convertToResponse(mark);
    }

    public MarkResponseTo update(MarkRequestTo markRequestTo) throws EntityNotFoundException{
        markDao.findById(markRequestTo.getId()).orElseThrow(() -> new EntityNotFoundException("This mark doesn't exist."));

        Mark updatedMark = convertToMark(markRequestTo);
        markDao.save(updatedMark);

        return convertToResponse(updatedMark);
    }

    public void delete(long id) {
        markDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Mark doesn't exist."));
        markDao.deleteById(id);
    }

    private Mark convertToMark(MarkRequestTo markRequestTo) {
        return this.modelMapper.map(markRequestTo, Mark.class);
    }

    private MarkResponseTo convertToResponse(Mark mark) {
        return this.modelMapper.map(mark, MarkResponseTo.class);
    }
}
