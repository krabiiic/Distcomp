package by.kardychka.Publisher.utils;


import by.kardychka.Publisher.DTOs.Requests.PostRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.PostResponseDTO;
import by.kardychka.Publisher.services.NewssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PostValidator implements Validator {
    private final NewssService newssService;

    @Autowired
    public PostValidator(NewssService newssService) {
        this.newssService = newssService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return PostResponseDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            PostRequestDTO post = (PostRequestDTO) target;
            if (!newssService.existsById(post.getNewsId())){
                errors.rejectValue("newsId", null, "News with such id doesn't exists");
            }
        }
    }
}
