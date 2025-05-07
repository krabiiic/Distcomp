package org.ikrotsyuk.bsuir.modulepublisher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.StickerRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.StickerResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.service.StickerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1.0/stickers")
public class StickerController {
    private final StickerService stickerService;

    @GetMapping
    public ResponseEntity<List<StickerResponseDTO>> getStickers(){
        return new ResponseEntity<>(stickerService.getStickers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StickerResponseDTO> getSticker(@PathVariable Long id){
        return new ResponseEntity<>(stickerService.getSticker(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StickerResponseDTO> addSticker(@Valid @RequestBody StickerRequestDTO stickerRequestDTO){
        return new ResponseEntity<>(stickerService.addSticker(stickerRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StickerResponseDTO> deleteSticker(@PathVariable Long id){
        return new ResponseEntity<>(stickerService.deleteSticker(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StickerResponseDTO> updateSticker(@PathVariable Long id, @Valid @RequestBody StickerRequestDTO stickerRequestDTO){
        return new ResponseEntity<>(stickerService.updateSticker(id, stickerRequestDTO), HttpStatus.OK);
    }
}
