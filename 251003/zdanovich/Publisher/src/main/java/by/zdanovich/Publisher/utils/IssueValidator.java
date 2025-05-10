package by.zdanovich.Publisher.utils;


import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class IssueValidator implements Validator {
    private final IssueService issueService;

    @Override
    public boolean supports(Class<?> clazz) {
        return IssueRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            IssueRequestDTO issue = (IssueRequestDTO) target;
            if (issueService.existsByTitle(issue.getTitle())) {
                errors.rejectValue("title", null, "Issue with such title already exists");
            }
        }
    }
}
