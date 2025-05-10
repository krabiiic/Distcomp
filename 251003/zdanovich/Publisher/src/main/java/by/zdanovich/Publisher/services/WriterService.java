package by.zdanovich.Publisher.services;

import by.zdanovich.Publisher.DTOs.Requests.WriterRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.WriterResponseDTO;
import by.zdanovich.Publisher.models.Writer;
import by.zdanovich.Publisher.repositories.WriterRepository;
import by.zdanovich.Publisher.utils.exceptions.NotFoundException;
import by.zdanovich.Publisher.utils.mappers.WriterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WriterService {
    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper;

    @Transactional
    public WriterResponseDTO save(WriterRequestDTO writerRequestDTO) {
        Writer writer = writerMapper.toWriter(writerRequestDTO);
        return writerMapper.toWriterResponse(writerRepository.save(writer));
    }

    @Transactional
    @CacheEvict(value = "writers", key = "#id")
    public void deleteById(long id) {
        if (!writerRepository.existsById(id)) {
            throw new NotFoundException("Writer not found");
        }
        writerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<WriterResponseDTO> findAll() {
        return writerMapper.toWriterResponseList(writerRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "writers", key = "#id")
    public WriterResponseDTO findById(long id){
        Writer writer = writerRepository.findById(id).get();//.orElseThrow(() -> new NotFoundException("Writer not found"));
        return writerMapper.toWriterResponse(writer);
    }

    @Transactional
    @CacheEvict(value = "writers", key = "#id")
    public WriterResponseDTO update(long id, WriterRequestDTO writerRequestDTO) {
        writerRequestDTO.setId(id);
        return update(writerRequestDTO);
    }

    @Transactional
    @CacheEvict(value = "writers", key = "#writerRequestDTO.id")
    public WriterResponseDTO update(WriterRequestDTO writerRequestDTO) {
        Writer writer = writerMapper.toWriter(writerRequestDTO);
        if (!writerRepository.existsById(writer.getId())) {
            throw new NotFoundException("Writer not found");
        }

        return writerMapper.toWriterResponse(writerRepository.save(writer));
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login){
        return writerRepository.existsByLogin(login);
    }
}
