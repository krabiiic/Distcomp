package by.kardychka.Publisher.utils;


import by.kardychka.Publisher.DTOs.Requests.NewsRequestDTO;
import by.kardychka.Publisher.services.NewssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NewsValidator implements Validator {
    private final NewssService newssService;

    @Autowired
    public NewsValidator(NewssService newssService) {
        this.newssService = newssService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return NewsRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasFieldErrors()){
            NewsRequestDTO news = (NewsRequestDTO) target;
            if (newssService.existsByTitle(news.getTitle())){
                errors.rejectValue("title", null, "News with such title already exists");
            }
        }
    }
}
