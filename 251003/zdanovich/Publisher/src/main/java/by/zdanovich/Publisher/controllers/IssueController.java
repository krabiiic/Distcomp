package by.zdanovich.Publisher.controllers;

import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.IssueResponseDTO;
import by.zdanovich.Publisher.services.IssueService;
import by.zdanovich.Publisher.utils.IssueValidator;
import by.zdanovich.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    private final IssueValidator issueValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDTO createIssue(@RequestBody @Valid IssueRequestDTO issueRequestDTO, BindingResult bindingResult) {
        validate(issueRequestDTO, bindingResult);
        return issueService.save(issueRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IssueResponseDTO> getAllIssues() {
        return issueService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDTO getByIssueById(@PathVariable Long id) {
        return issueService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable long id) {
        issueService.deleteById(id);
    }


    // Non REST version for tests compliance
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDTO updateIssue(@RequestBody @Valid IssueRequestDTO issueRequestDTO) {
        return issueService.update(issueRequestDTO);
    }

    private void validate(IssueRequestDTO issueRequestDTO, BindingResult bindingResult) {
        issueValidator.validate(issueRequestDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
