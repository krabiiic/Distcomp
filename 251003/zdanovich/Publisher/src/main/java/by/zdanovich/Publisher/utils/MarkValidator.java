package by.zdanovich.Publisher.utils;

import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.DTOs.Requests.MarkRequestDTO;
import by.zdanovich.Publisher.services.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MarkValidator implements Validator {
    private final MarkService markService;

    @Override
    public boolean supports(Class<?> clazz) {
        return IssueRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            MarkRequestDTO markRequestDTO = (MarkRequestDTO) target;
            if (markService.existsByName(markRequestDTO.getName())) {
                errors.rejectValue("name", null, "Mark with such name already exists");
            }
        }
    }
}
