using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;

namespace DistComp_1.Services.Implementations;

public class MessageService : IMessageService
{
    private readonly IMessageRepository _MessageRepository;
    private readonly IMapper _mapper;
    private readonly MessageRequestDTOValidator _validator;
    
    public MessageService(IMessageRepository MessageRepository, 
        IMapper mapper, MessageRequestDTOValidator validator)
    {
        _MessageRepository = MessageRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<MessageResponseDTO>> GetMessagesAsync()
    {
        var Messages = await _MessageRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<MessageResponseDTO>>(Messages);
    }

    public async Task<MessageResponseDTO> GetMessageByIdAsync(long id)
    {
        var Message = await _MessageRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        return _mapper.Map<MessageResponseDTO>(Message);
    }

    public async Task<MessageResponseDTO> CreateMessageAsync(MessageRequestDTO Message)
    {
        await _validator.ValidateAndThrowAsync(Message);
        var MessageToCreate = _mapper.Map<Message>(Message);
        var createdMessage = await _MessageRepository.CreateAsync(MessageToCreate);
        return _mapper.Map<MessageResponseDTO>(createdMessage);
    }

    public async Task<MessageResponseDTO> UpdateMessageAsync(MessageRequestDTO Message)
    {
        await _validator.ValidateAndThrowAsync(Message);
        var MessageToUpdate = _mapper.Map<Message>(Message);
        var updatedMessage = await _MessageRepository.UpdateAsync(MessageToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(Message.Id));
        return _mapper.Map<MessageResponseDTO>(updatedMessage);
    }

    public async Task DeleteMessageAsync(long id)
    {
        if (await _MessageRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        }
    }
}
