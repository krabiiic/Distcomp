package by.ustsinovich.distcomp.lab1.service.impl;

import by.ustsinovich.distcomp.lab1.mapper.IssueMapper;
import by.ustsinovich.distcomp.lab1.repository.IssueRepository;
import by.ustsinovich.distcomp.lab1.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

}
