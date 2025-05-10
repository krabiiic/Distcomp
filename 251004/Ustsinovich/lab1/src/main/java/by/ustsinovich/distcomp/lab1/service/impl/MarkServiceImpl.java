package by.ustsinovich.distcomp.lab1.service.impl;

import by.ustsinovich.distcomp.lab1.mapper.MarkMapper;
import by.ustsinovich.distcomp.lab1.repository.MarkRepository;
import by.ustsinovich.distcomp.lab1.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;

    private final MarkMapper markMapper;

}
