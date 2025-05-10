package by.kapinskiy.Publisher.utils;

import by.kapinskiy.Publisher.DTOs.Requests.IssueRequestDTO;
import by.kapinskiy.Publisher.DTOs.Requests.TagRequestDTO;
import by.kapinskiy.Publisher.services.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TagValidator implements Validator {
    private final TagsService tagsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return IssueRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            TagRequestDTO tag = (TagRequestDTO) target;
            if (tagsService.existsByName(tag.getName())) {
                errors.rejectValue("name", null, "Tag with such name already exists");
            }
        }
    }
}
