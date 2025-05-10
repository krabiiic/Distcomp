package by.zdanovich.Publisher.controllers;

import by.zdanovich.Publisher.DTOs.Requests.WriterRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.WriterResponseDTO;
import by.zdanovich.Publisher.services.WriterService;
import by.zdanovich.Publisher.utils.WriterValidator;
import by.zdanovich.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/writers")
@RequiredArgsConstructor
public class WriterController {
    private final WriterService writerService;
    private final WriterValidator writerValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WriterResponseDTO createWriter(@RequestBody @Valid WriterRequestDTO WriterRequestDTO, BindingResult bindingResult) throws MethodArgumentNotValidException {
        validate(WriterRequestDTO, bindingResult);
        return writerService.save(WriterRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWriter(@PathVariable long id){
        writerService.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WriterResponseDTO> getAllWriters(){
        return writerService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WriterResponseDTO getWriter(@PathVariable long id) {
        WriterResponseDTO writerResponseDTO = null;
        try {
            writerResponseDTO =  writerService.findById(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return writerResponseDTO;
    }

    // Because of test limitations we don't require id in the path, and also we send Writer in the response,
    // but we shouldn't do that
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public WriterResponseDTO updateWriter(@RequestBody @Valid WriterRequestDTO WriterRequestDTO){
        return writerService.update(WriterRequestDTO);
    }

    // Rest version
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Long updateWriter(@PathVariable Long id, @RequestBody @Valid WriterRequestDTO WriterRequestDTO){
        return writerService.update(id, WriterRequestDTO).getId();
    }

    private void validate(WriterRequestDTO WriterRequestDTO, BindingResult bindingResult){
        writerValidator.validate(WriterRequestDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
