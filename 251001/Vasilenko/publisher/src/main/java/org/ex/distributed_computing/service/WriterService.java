package org.ex.distributed_computing.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.WriterRequestDTO;
import org.ex.distributed_computing.dto.response.WriterResponseDTO;
import org.ex.distributed_computing.exception.DuplicateDatabaseValueException;
import org.ex.distributed_computing.exception.NotFoundException;
import org.ex.distributed_computing.mapper.WriterMapper;
import org.ex.distributed_computing.model.Writer;
import org.ex.distributed_computing.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriterService {

  private final WriterRepository writerRepository;
  private final WriterMapper writerMapper;

  @Qualifier("writerCache")
  private final Cache cache;

  public List<WriterResponseDTO> getAllWriters() {
    return writerMapper.toDtoList(writerRepository.findAll());
  }

  public WriterResponseDTO getWriterById(Long id) {
    var cached = cache.get(id, WriterResponseDTO.class);
    if (cached != null) {
      return cached;
    }

    var writer = writerRepository.findById(id)
            .map(writerMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Writer not found"));

    cache.put(id, writer);
    return writer;
  }

  public WriterResponseDTO createWriter(WriterRequestDTO requestDTO) {
    Writer writer = writerMapper.toEntity(requestDTO);
    if (writerRepository.existsByLogin(writer.getLogin())) {
      throw new DuplicateDatabaseValueException();
    }
    writerRepository.save(writer);
    var writerDto = writerMapper.toDto(writer);
    cache.put(writer.getId(), writerDto);
    return writerDto;
  }

  public WriterResponseDTO updateWriter(WriterRequestDTO dto) {
    Writer writer = writerRepository.findById(dto.id())
            .orElseThrow(() -> new NotFoundException("Writer not found"));

    writer.setLogin(dto.login());
    writer.setPassword(dto.password());
    writer.setFirstname(dto.firstname());
    writer.setLastname(dto.lastname());

    writerRepository.save(writer);
    var writerDto = writerMapper.toDto(writer);
    cache.put(writer.getId(), writerDto);
    return writerDto;
  }

  public void deleteWriter(Long id) {
    Writer writer = writerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Writer not found"));
    writerRepository.delete(writer);
    cache.evictIfPresent(id);
  }
}