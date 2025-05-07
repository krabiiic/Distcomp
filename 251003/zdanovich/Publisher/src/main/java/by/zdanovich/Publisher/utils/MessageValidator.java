package by.zdanovich.Publisher.utils;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Publisher.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MessageValidator implements Validator {
    private final IssueService issueService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MessageResponseDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()) {
            MessageRequestDTO messageRequestDTO = (MessageRequestDTO) target;
            if (!issueService.existsById(messageRequestDTO.getIssueId())) {
                errors.rejectValue("issueId", null, "Issue with such id doesn't exists");
            }
        }
    }
}
