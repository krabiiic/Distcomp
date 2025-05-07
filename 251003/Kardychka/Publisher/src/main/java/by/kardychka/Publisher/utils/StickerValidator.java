package by.kardychka.Publisher.utils;

import by.kardychka.Publisher.DTOs.Requests.NewsRequestDTO;
import by.kardychka.Publisher.DTOs.Requests.StickerRequestDTO;
import by.kardychka.Publisher.services.StickersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StickerValidator implements Validator {
    private final StickersService stickersService;

    @Autowired
    public StickerValidator(StickersService stickersService) {
        this.stickersService = stickersService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return NewsRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            StickerRequestDTO sticker = (StickerRequestDTO) target;
            if (stickersService.existsByName(sticker.getName())){
                errors.rejectValue("title", null, "Sticker with such name already exists");
            }
        }
    }
}
