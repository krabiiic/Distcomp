package com.example.publisher.api.controller.impl;

import com.example.publisher.api.controller.IssueController;
import com.example.publisher.api.dto.request.IssueRequestTo;
import com.example.publisher.api.dto.responce.IssueResponseTo;
import com.example.publisher.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IssueControllerImpl implements IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueControllerImpl(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    public IssueResponseTo create(IssueRequestTo request) {
        return issueService.create(request);
    }

    @Override
    public List<IssueResponseTo> getAll() {
        return issueService.getAll();
    }

    @Override
    public IssueResponseTo getById(Long id) {
        return issueService.getById(id);
    }

    @Override
    public IssueResponseTo update(IssueRequestTo request) {
        return issueService.update(request);
    }

    @Override
    public void delete(Long id) {
        issueService.delete(id);
    }
}
