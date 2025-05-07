package org.ikrotsyuk.bsuir.modulepublisher.service;

import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.StickerRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.StickerResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.StickerEntity;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.mapper.StickerMapper;
import org.ikrotsyuk.bsuir.modulepublisher.repository.StickerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StickerService {
    private final StickerMapper stickerMapper;
    private final StickerRepository stickerRepository;

    @Cacheable(value = "stickers")
    @Transactional(readOnly = true)
    public List<StickerResponseDTO> getStickers(){
        List<StickerEntity> stickerEntityList = stickerRepository.findAll();
        if(stickerEntityList.isEmpty())
            return Collections.emptyList();
        else
            return stickerMapper.toDTOList(stickerEntityList);
    }

    @Cacheable(value = "stickers", key = "#id")
    @Transactional(readOnly = true)
    public StickerResponseDTO getSticker(Long id){
        Optional<StickerEntity> optionalStickerEntity = stickerRepository.findById(id);
        return optionalStickerEntity.map(stickerMapper::toDTO).orElse(null);
    }

    @CacheEvict(value = "stickers", allEntries = true)
    @Transactional
    public StickerResponseDTO addSticker(StickerRequestDTO stickerRequestDTO){
        StickerEntity stickerEntity = stickerMapper.toEntity(stickerRequestDTO);
        return stickerMapper.toDTO(stickerRepository.save(stickerEntity));
    }

    @CacheEvict(value = "stickers", key = "#id")
    @Transactional
    public StickerResponseDTO deleteSticker(Long id){
        Optional<StickerEntity> optionalStickerEntity = stickerRepository.findById(id);
        if(optionalStickerEntity.isPresent()){
            StickerResponseDTO sticker = stickerMapper.toDTO(optionalStickerEntity.get());
            stickerRepository.deleteById(id);
            return sticker;
        } else
            throw new NotFoundException();
    }

    @CacheEvict(value = "stickers", key = "#id")
    @Transactional
    public StickerResponseDTO updateSticker(Long id, StickerRequestDTO stickerRequestDTO){
        Optional<StickerEntity> optionalStickerEntity = stickerRepository.findById(id);
        if(optionalStickerEntity.isPresent()){
            StickerEntity stickerEntity = optionalStickerEntity.get();
            stickerEntity.setName(stickerRequestDTO.getName());
            return stickerMapper.toDTO(stickerRepository.save(stickerEntity));
        } else
            throw new NotFoundException();
    }
}
