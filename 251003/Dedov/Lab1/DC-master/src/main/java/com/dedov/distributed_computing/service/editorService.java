package com.dedov.distributed_computing.service;

import com.dedov.distributed_computing.dao.InMemorynewsDao;
import com.dedov.distributed_computing.dao.InMemoryeditorDao;
import com.dedov.distributed_computing.dto.request.editorRequestTo;
import com.dedov.distributed_computing.dto.response.editorResponseTo;
import com.dedov.distributed_computing.exception.DuplicateFieldException;
import com.dedov.distributed_computing.exception.EntityNotFoundException;
import com.dedov.distributed_computing.model.news;
import com.dedov.distributed_computing.model.editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class editorService {

    private final ModelMapper modelMapper;
    private final InMemoryeditorDao editorDao;
    private final InMemorynewsDao newsDao;

    @Autowired
    public editorService(ModelMapper modelMapper, InMemoryeditorDao editorDao, InMemorynewsDao newsDao) {
        this.modelMapper = modelMapper;
        this.editorDao = editorDao;
        this.newsDao = newsDao;
    }

    public List<editorResponseTo> findAll() {
        return editorDao.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public editorResponseTo findById(long id) throws EntityNotFoundException {
        editor editor = editorDao.findById(id).orElseThrow(() -> new EntityNotFoundException("This editor doesn't exist."));

        return convertToResponse(editor);
    }

    public editorResponseTo findBynewsId(long newsId) throws EntityNotFoundException {
        news news = newsDao.findById(newsId).orElseThrow(() -> new EntityNotFoundException("news with id " + newsId + " doesn't exist."));
        editor editor = editorDao.findById(news.geteditorId()).orElseThrow(() -> new EntityNotFoundException("No editor found with news id " + newsId));
        return convertToResponse(editor);
    }

    public editorResponseTo save(editorRequestTo editorRequestTo) throws DuplicateFieldException {

        if (editorDao.findByLogin(editorRequestTo.getLogin()).isPresent()) {
            throw new DuplicateFieldException("login", editorRequestTo.getLogin());
        }

        editor editor = convertToeditor(editorRequestTo);
        editorDao.save(editor);
        return convertToResponse(editor);
    }

    public editorResponseTo update(editorRequestTo editorRequestTo) throws EntityNotFoundException, DuplicateFieldException {
        editorDao.findById(editorRequestTo.getId()).orElseThrow(() -> new EntityNotFoundException("This editor doesn't exist."));

        editorDao.findByLogin(editorRequestTo.getLogin())
                .filter(editor -> editor.getId() != editorRequestTo.getId())
                .ifPresent(editor -> {
                    throw new DuplicateFieldException("login", editorRequestTo.getLogin());
                });

        editor updatededitor = convertToeditor(editorRequestTo);
        editorDao.save(updatededitor);
        return convertToResponse(updatededitor);
    }

    public editorResponseTo partialUpdate(editorRequestTo editorRequestTo) throws EntityNotFoundException, DuplicateFieldException {

        editor editor = editorDao.findById(editorRequestTo.getId()).orElseThrow(() -> new EntityNotFoundException("editor not found"));

        if (editorRequestTo.getLogin() != null && !editorRequestTo.getLogin().equals(editor.getLogin())) {
            if (editorDao.findByLogin(editorRequestTo.getLogin()).isPresent()) {
                throw new DuplicateFieldException("login", editorRequestTo.getLogin());
            }
            editor.setLogin(editorRequestTo.getLogin());
        }
        if (editorRequestTo.getFirstname() != null) {
            editor.setFirstname(editorRequestTo.getFirstname());
        }
        if (editorRequestTo.getLastname() != null) {
            editor.setLastname(editorRequestTo.getLastname());
        }
        if (editorRequestTo.getPassword() != null) {
            editor.setPassword(editorRequestTo.getPassword());
        }

        editorDao.save(editor);

        return convertToResponse(editor);
    }

    public void delete(long id) throws EntityNotFoundException {
        editorDao.findById(id).orElseThrow(() -> new EntityNotFoundException("This editor doesn't exist."));
        editorDao.deleteById(id);
    }


    private editor convertToeditor(editorRequestTo editorRequestTo) {
        return this.modelMapper.map(editorRequestTo, editor.class);
    }
    private editorResponseTo convertToResponse(editor editor) {
        return this.modelMapper.map(editor, editorResponseTo.class);
    }
}
