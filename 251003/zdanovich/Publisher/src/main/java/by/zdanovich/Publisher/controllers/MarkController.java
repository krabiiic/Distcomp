package by.zdanovich.Publisher.controllers;


import by.zdanovich.Publisher.DTOs.Requests.MarkRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MarkResponseDTO;
import by.zdanovich.Publisher.services.MarkService;
import by.zdanovich.Publisher.utils.MarkValidator;
import by.zdanovich.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marks")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;

    private final MarkValidator markValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarkResponseDTO createMark(@RequestBody @Valid MarkRequestDTO markRequestDTO, BindingResult bindingResult) {
        validate(markRequestDTO, bindingResult);
        return markService.save(markRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MarkResponseDTO> getAllMarks() {
        return markService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MarkResponseDTO getMarkById(@PathVariable Long id) {
        return markService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMark(@PathVariable long id){
        markService.deleteById(id);
    }

    // Non REST version for test compliance
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public MarkResponseDTO updateMark(@RequestBody @Valid MarkRequestDTO markRequestDTO){
        return markService.update(markRequestDTO);
    }

    private void validate(MarkRequestDTO markRequestDTO, BindingResult bindingResult){
        markValidator.validate(markRequestDTO, bindingResult);
        if (bindingResult.hasFieldErrors()){
            throw new ValidationException(bindingResult);
        }
    }
}
