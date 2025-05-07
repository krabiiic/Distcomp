using AutoMapper;
using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;
using Discussion.Exceptions;
using Discussion.Infrastructure.Validators;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using Discussion.Services.Interfaces;
using FluentValidation;

namespace Discussion.Services.Implementations;

public class MessageService : IMessageService
{
    private readonly IMessageRepository _messageRepository;
    private readonly IMapper _mapper;
    private readonly MessageRequestDtoValidator _validator;
    
    public MessageService(IMessageRepository messageRepository, 
        IMapper mapper, MessageRequestDtoValidator validator)
    {
        _messageRepository = messageRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<MessageResponseDTO>> GetMessagesAsync()
    {
        var messages = await _messageRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<MessageResponseDTO>>(messages);
    }

    public async Task<MessageResponseDTO> GetMessageByIdAsync(long id)
    {
        var message = await _messageRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        return _mapper.Map<MessageResponseDTO>(message);
    }

    public async Task<MessageResponseDTO> CreateMessageAsync(MessageRequestDTO message)
    {
        await _validator.ValidateAndThrowAsync(message);
        var messageToCreate = _mapper.Map<Message>(message);
        var createdMessage = await _messageRepository.CreateAsync(messageToCreate);
        return _mapper.Map<MessageResponseDTO>(createdMessage);
    }

    public async Task<MessageResponseDTO> UpdateMessageAsync(MessageRequestDTO message)
    {
        await _validator.ValidateAndThrowAsync(message);
        var messageToUpdate = _mapper.Map<Message>(message);
        var updatedMessage = await _messageRepository.UpdateAsync(messageToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(message.Id));
        return _mapper.Map<MessageResponseDTO>(updatedMessage);
    }

    public async Task DeleteMessageAsync(long id)
    {
        if (!await _messageRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        }
    }
}
