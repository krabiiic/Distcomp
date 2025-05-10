package by.kardychka.Publisher.controllers;


import by.kardychka.Publisher.DTOs.Requests.NewsRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.NewsResponseDTO;
import by.kardychka.Publisher.services.NewssService;
import by.kardychka.Publisher.utils.NewsValidator;
import by.kardychka.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewssController {
    private final NewssService newssService;
    private final NewsValidator newsValidator;

    @Autowired
    public NewssController(NewssService newssService, NewsValidator newsValidator) {
        this.newssService = newssService;
        this.newsValidator = newsValidator;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsResponseDTO createNews(@RequestBody @Valid NewsRequestDTO newsRequestDTO, BindingResult bindingResult) {
        validate(newsRequestDTO, bindingResult);
        return newssService.save(newsRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponseDTO> getAllNewss() {
        return newssService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsResponseDTO getByNewsById(@PathVariable Long id) {
        return newssService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable long id){
        newssService.deleteById(id);
    }


    // Non REST version for tests compliance
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsResponseDTO updateNews(@RequestBody @Valid NewsRequestDTO newsRequestDTO, BindingResult bindingResult){
        validate(newsRequestDTO, bindingResult);
        return newssService.update(newsRequestDTO);
    }

    private void validate(NewsRequestDTO newsRequestDTO, BindingResult bindingResult){
        newsValidator.validate(newsRequestDTO, bindingResult);
        if (bindingResult.hasFieldErrors()){
            throw new ValidationException(bindingResult);
        }
    }
}
