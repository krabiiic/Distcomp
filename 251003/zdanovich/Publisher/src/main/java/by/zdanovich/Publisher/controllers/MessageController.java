package by.zdanovich.Publisher.controllers;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Publisher.services.MessageService;
import by.zdanovich.Publisher.utils.MessageValidator;
import by.zdanovich.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    private final MessageValidator messageValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createMessage(@RequestBody @Valid MessageRequestDTO MessageRequestDTO,
                                         BindingResult bindingResult) {
        validateRequest(MessageRequestDTO, bindingResult);
        return messageService.createMessage(MessageRequestDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateMessage(
                                      @RequestBody @Valid MessageRequestDTO messageRequestDTO) {
        return messageService.processMessageRequest("PUT", messageRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        MessageRequestDTO request = new MessageRequestDTO();
        request.setId(id);
        messageService.processMessageRequest("DELETE", request);
    }

    private void validateRequest(MessageRequestDTO request, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            messageValidator.validate(request, bindingResult);
        }
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}