using Core.DTO;
using Core.Interfaces;
using AutoMapper;
using Core.Entities;
using Core.Exceptions;

namespace Core.Services;

public class MessageService(IMessageRepository _messageRepository,IMapper _mapper) : IMessageService
{
    public async Task<MessageResponseToGetById?> CreateMessage(MessageRequestToCreate model)
    {
        Message message = _mapper.Map<Message>(model);
        Validate(message);
        message = await _messageRepository.AddMessage(message);
        return _mapper.Map<MessageResponseToGetById>(message);
    }

    public async Task<IEnumerable<MessageResponseToGetById?>?> GetMessages(MessageRequestToGetAll request)
    {
        var message = await _messageRepository.GetAllMessages();
        return message.Select(_mapper.Map<MessageResponseToGetById>);
    }

    public async Task<IEnumerable<MessageResponseToGetById?>?> GetMessagesByArticleId(MessageRequestToGetByArticleId request)
    {
        var messages = await _messageRepository.GetMessagesByArticleId(request.ArticleId);
        return messages.Select(_mapper.Map<MessageResponseToGetById>);
    }

    public async Task<MessageResponseToGetById?> GetMessageById(MessageRequestToGetById request)
    {
        var message = await _messageRepository.GetMessage(request.Id);
        return _mapper.Map<MessageResponseToGetById>(message);
    }

    public async Task<MessageResponseToGetById?> UpdateMessage(MessageRequestToFullUpdate model)
    {
        var message = _mapper.Map<Message>(model);
        Validate(message);
        message = await _messageRepository.UpdateMessage(message);
        return _mapper.Map<MessageResponseToGetById>(message);
    }

    public async Task<MessageResponseToGetById?> DeleteMessage(MessageRequestToDeleteById request)
    {
        var message = await _messageRepository.RemoveMessage(request.Id);
        return _mapper.Map<MessageResponseToGetById>(message);
    }
    
    private bool Validate(Message message)
    {
        var errors = new Dictionary<string, string[]>();
        if (message.Content.Length < 2 || message.Content.Length > 2048)
        {
            errors.Add("Content",["Should be from 2 to 64 chars"]);
        }
        if (errors.Count != 0)
        {
            throw new BadRequestException("Validation error", errors);
        }
        return true;
    }
}