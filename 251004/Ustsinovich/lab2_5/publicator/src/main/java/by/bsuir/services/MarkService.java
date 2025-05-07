package by.bsuir.services;

import by.bsuir.dto.MarkRequestTo;
import by.bsuir.dto.MarkResponseTo;
import by.bsuir.entities.Mark;
import by.bsuir.exceptions.DeleteException;
import by.bsuir.exceptions.NotFoundException;
import by.bsuir.exceptions.UpdateException;
import by.bsuir.mapper.MarkListMapper;
import by.bsuir.mapper.MarkMapper;
import by.bsuir.repository.MarkRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@CacheConfig(cacheNames = "markCache")
public class MarkService {
    @Autowired
    MarkMapper markMapper;
    @Autowired
    MarkRepository markDao;
    @Autowired
    MarkListMapper markListMapper;
    @Cacheable(cacheNames = "marks", key = "#id", unless = "#result == null")
    public MarkResponseTo getMarkById(@Min(0) Long id) throws NotFoundException {
        Optional<Mark> mark = markDao.findById(id);
        return mark.map(value -> markMapper.markToMarkResponse(value)).orElseThrow(() -> new NotFoundException("Mark not found!", 40004L));
    }
    @Cacheable(cacheNames = "marks")
    public List<MarkResponseTo> getMarks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<Mark> marks = markDao.findAll(pageable);
        return markListMapper.toMarkResponseList(marks.toList());
    }
    @CacheEvict(cacheNames = "marks", allEntries = true)
    public MarkResponseTo saveMark(@Valid MarkRequestTo mark) {
        Mark markToSave = markMapper.markRequestToMark(mark);
        return markMapper.markToMarkResponse(markDao.save(markToSave));
    }
    @Caching(evict = { @CacheEvict(cacheNames = "marks", key = "#id"),
            @CacheEvict(cacheNames = "marks", allEntries = true) })
    public void deleteMark(@Min(0) Long id) throws DeleteException {
        if (!markDao.existsById(id)) {
            throw new DeleteException("Mark not found!", 40004L);
        } else {
            markDao.deleteById(id);
        }
    }
    @CacheEvict(cacheNames = "marks", allEntries = true)
    public MarkResponseTo updateMark(@Valid MarkRequestTo mark) throws UpdateException {
        Mark markToUpdate = markMapper.markRequestToMark(mark);
        if (!markDao.existsById(markToUpdate.getId())) {
            throw new UpdateException("Mark not found!", 40004L);
        } else {
            return markMapper.markToMarkResponse(markDao.save(markToUpdate));
        }
    }

    public List<MarkResponseTo> getMarkByIssueId(@Min(0) Long issueId) throws NotFoundException {
        List<Mark> mark = markDao.findMarksByIssueId(issueId);
        return markListMapper.toMarkResponseList(mark);
    }
}
