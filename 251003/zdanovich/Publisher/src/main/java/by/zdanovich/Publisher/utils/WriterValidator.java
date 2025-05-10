package by.zdanovich.Publisher.utils;

import by.zdanovich.Publisher.DTOs.Requests.WriterRequestDTO;
import by.zdanovich.Publisher.services.WriterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class WriterValidator implements Validator {
    private final WriterService writerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return WriterRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            WriterRequestDTO writerRequestDTO = (WriterRequestDTO) target;
            if (writerService.existsByLogin(writerRequestDTO.getLogin())) {
                errors.rejectValue("login", null, "Writer with such login already exists");
            }
        }
    }
}
