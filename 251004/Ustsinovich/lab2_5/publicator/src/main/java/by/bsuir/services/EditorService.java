package by.bsuir.services;

import by.bsuir.dto.EditorRequestTo;
import by.bsuir.dto.EditorResponseTo;
import by.bsuir.entities.Editor;
import by.bsuir.exceptions.DeleteException;
import by.bsuir.exceptions.DuplicationException;
import by.bsuir.exceptions.NotFoundException;
import by.bsuir.exceptions.UpdateException;
import by.bsuir.mapper.EditorListMapper;
import by.bsuir.mapper.EditorMapper;
import by.bsuir.repository.EditorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
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
@CacheConfig(cacheNames = "editorsCache")
public class EditorService {
    @Autowired
    EditorMapper editorMapper;
    @Autowired
    EditorRepository editorDao;
    @Autowired
    EditorListMapper editorListMapper;

    @Cacheable(cacheNames = "editors", key = "#id", unless = "#result == null")
    public EditorResponseTo getEditorById(@Min(0) Long id) throws NotFoundException {
        Optional<Editor> editor = editorDao.findById(id);
        return editor.map(value -> editorMapper.editorToEditorResponse(value)).orElseThrow(() -> new NotFoundException("Editor not found!", 40004L));
    }

    @Cacheable(cacheNames = "editors")
    public List<EditorResponseTo> getEditors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<Editor> editors = editorDao.findAll(pageable);
        return editorListMapper.toEditorResponseList(editors.toList());
    }
    @CacheEvict(cacheNames = "editors", allEntries = true)
    public EditorResponseTo saveEditor(@Valid EditorRequestTo editor) throws DuplicationException {
        Editor editorToSave = editorMapper.editorRequestToEditor(editor);
        if (editorDao.existsByLogin(editorToSave.getLogin())) {
            throw new DuplicationException("Login duplication", 40005L);
        }
        return editorMapper.editorToEditorResponse(editorDao.save(editorToSave));
    }
    @Caching(evict = { @CacheEvict(cacheNames = "editors", key = "#id"),
            @CacheEvict(cacheNames = "editors", allEntries = true) })
    public void deleteEditor(@Min(0) Long id) throws DeleteException {
        if (!editorDao.existsById(id)) {
            throw new DeleteException("Editor not found!", 40004L);
        } else {
            editorDao.deleteById(id);
        }
    }
    @CacheEvict(cacheNames = "editors", allEntries = true)
    public EditorResponseTo updateEditor(@Valid EditorRequestTo editor) throws UpdateException {
        Editor editorToUpdate = editorMapper.editorRequestToEditor(editor);
        if (!editorDao.existsById(editorToUpdate.getId())) {
            throw new UpdateException("Editor not found!", 40004L);
        } else {
            return editorMapper.editorToEditorResponse(editorDao.save(editorToUpdate));
        }
    }

    public EditorResponseTo getEditorByIssueId(@Min(0) Long issueId) throws NotFoundException {
        Editor editor = editorDao.findEditorByIssueId(issueId);
        return editorMapper.editorToEditorResponse(editor);
    }
}
