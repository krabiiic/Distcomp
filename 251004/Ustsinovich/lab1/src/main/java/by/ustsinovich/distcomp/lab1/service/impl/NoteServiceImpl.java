package by.ustsinovich.distcomp.lab1.service.impl;

import by.ustsinovich.distcomp.lab1.mapper.NoteMapper;
import by.ustsinovich.distcomp.lab1.repository.NoteRepository;
import by.ustsinovich.distcomp.lab1.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

}
