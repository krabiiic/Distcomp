package com.example.discussion.service;

import com.example.discussion.dto.WriterRequestTo;
import com.example.discussion.dto.WriterResponseTo;
import com.example.discussion.exception.DuplicateLoginException;
import com.example.discussion.model.Writer;
import com.example.discussion.repository.WriterRepository;
import com.example.discussion.service.mapper.WriterMapper;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WriterService {

    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper = Mappers.getMapper(WriterMapper.class);

    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public List<WriterResponseTo> findAll() {
        return writerRepository.findAll().stream()
                .map(writerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WriterResponseTo findById(Long id) {
        Optional<Writer> writer = writerRepository.findById(id);
        return writer.map(writerMapper::toDto).orElse(null);
    }

    @Transactional
    public WriterResponseTo save(WriterRequestTo writerRequestTo) {
        if (writerRepository.findByLogin(writerRequestTo.getLogin()).isPresent()) {
            throw new DuplicateLoginException("Login already exists");
        }
        Writer writer = writerMapper.toEntity(writerRequestTo);
        Writer savedWriter = writerRepository.save(writer);
        return writerMapper.toDto(savedWriter);
    }

    @Transactional
    public WriterResponseTo update(WriterRequestTo writerRequestTo) {
        Writer existingWriter = writerRepository.findById(writerRequestTo.getId()).orElseThrow(() -> new RuntimeException("Writer not found"));
        writerMapper.updateEntityFromDto(writerRequestTo, existingWriter);
        Writer updatedWriter = writerRepository.save(existingWriter);
        return writerMapper.toDto(updatedWriter);
    }

    public void deleteById(Long id) {
        if (!writerRepository.existsById(id)) {
            throw new EntityNotFoundException("Writer not found with id " + id);
        }
        writerRepository.deleteById(id);
    }
}