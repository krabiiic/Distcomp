package org.ikrotsyuk.bsuir.modulepublisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.WriterRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.WriterResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.WriterEntity;
import org.ikrotsyuk.bsuir.modulepublisher.exception.JSONConverter;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.UserWithSameLoginFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.mapper.WriterMapper;
import org.ikrotsyuk.bsuir.modulepublisher.repository.WriterRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WriterService {
    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper;
    private final JSONConverter jsonConverter;

    @Cacheable(value = "writers")
    @Transactional(readOnly = true)
    public List<WriterResponseDTO> getWriters(){
        List<WriterEntity> writerEntityList = writerRepository.findAll();
        if(writerEntityList.isEmpty())
            return Collections.emptyList();
        else
            return writerMapper.toDTOList(writerEntityList);
    }

    @Cacheable(value = "writers", key = "#id")
    @Transactional(readOnly = true)
    public WriterResponseDTO getWriterById(long id){
        Optional<WriterEntity> writerEntityOptional = writerRepository.findById(id);
        return writerEntityOptional.map(writerMapper::toDTO).orElse(null);
    }

    @CacheEvict(value = "writers", allEntries = true)
    @Transactional
    public WriterResponseDTO addWriter(WriterRequestDTO writerRequestDTO) {
        Optional<WriterEntity> optionalWriterEntity = writerRepository.findByLogin(writerRequestDTO.getLogin());
        if(optionalWriterEntity.isEmpty())
            return writerMapper.toDTO(writerRepository.save(writerMapper.toEntity(writerRequestDTO)));
        else {
            try{
                throw new UserWithSameLoginFoundException(jsonConverter.convertObjectToJSON(writerRequestDTO));
            }catch (JsonProcessingException ex){
                return writerMapper.toDTO(optionalWriterEntity.get());
            }
        }
    }

    @CacheEvict(value = "writers", key = "#id")
    @Transactional
    public WriterResponseDTO deleteWriter(Long id){
        Optional<WriterEntity> optionalWriterEntity = writerRepository.findById(id);
        if(optionalWriterEntity.isPresent()) {
            writerRepository.deleteById(id);
            return writerMapper.toDTO(optionalWriterEntity.get());
        }else{
            throw new NotFoundException();
        }
    }

    @CacheEvict(value = "writers", allEntries = true)
    @Transactional
    public WriterResponseDTO updateWriter(Long id, WriterRequestDTO writerRequestDTO){
        Optional<WriterEntity> optionalWriterEntity = writerRepository.findById(id);
        if(optionalWriterEntity.isPresent()){
            WriterEntity writerEntity = optionalWriterEntity.get();
            writerEntity.setLogin(writerRequestDTO.getLogin());
            writerEntity.setPassword(writerRequestDTO.getPassword());
            writerEntity.setFirstname(writerRequestDTO.getFirstname());
            writerEntity.setLastname(writerRequestDTO.getLastname());
            return writerMapper.toDTO(writerRepository.save(writerEntity));
        }else
            throw new NotFoundException();
    }
}
