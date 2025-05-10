package by.kardychka.Publisher.controllers;


import by.kardychka.Publisher.DTOs.Requests.StickerRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.StickerResponseDTO;
import by.kardychka.Publisher.services.StickersService;
import by.kardychka.Publisher.utils.StickerValidator;
import by.kardychka.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stickers")
public class StickersController {
    private final StickersService stickersService;

    private final StickerValidator stickerValidator;

    @Autowired
    public StickersController(StickersService stickersService, StickerValidator stickerValidator) {
        this.stickersService = stickersService;
        this.stickerValidator = stickerValidator;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StickerResponseDTO createSticker(@RequestBody @Valid StickerRequestDTO stickerRequestDTO, BindingResult bindingResult) {
        validate(stickerRequestDTO, bindingResult);
        return stickersService.save(stickerRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StickerResponseDTO> getAllStickers() {
        return stickersService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StickerResponseDTO getStickerById(@PathVariable Long id) {
        return stickersService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSticker(@PathVariable long id){
        stickersService.deleteById(id);
    }

    // Non REST version for test compliance
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StickerResponseDTO updateSticker(@RequestBody @Valid StickerRequestDTO stickerRequestDTO, BindingResult bindingResult){
        validate(stickerRequestDTO, bindingResult);
        return stickersService.update(stickerRequestDTO);
    }

    private void validate(StickerRequestDTO stickerRequestDTO, BindingResult bindingResult){
        stickerValidator.validate(stickerRequestDTO, bindingResult);
        if (bindingResult.hasFieldErrors()){
            throw new ValidationException(bindingResult);
        }
    }
}
