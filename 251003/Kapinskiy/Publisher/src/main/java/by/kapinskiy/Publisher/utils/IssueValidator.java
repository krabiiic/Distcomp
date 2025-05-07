package by.kapinskiy.Publisher.utils;


import by.kapinskiy.Publisher.DTOs.Requests.IssueRequestDTO;
import by.kapinskiy.Publisher.services.IssuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class IssueValidator implements Validator {
    private final IssuesService issuesService;

    @Override
    public boolean supports(Class<?> clazz) {
        return IssueRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            IssueRequestDTO issue = (IssueRequestDTO) target;
            if (issuesService.existsByTitle(issue.getTitle())) {
                errors.rejectValue("title", null, "Issue with such title already exists");
            }
        }
    }
}
