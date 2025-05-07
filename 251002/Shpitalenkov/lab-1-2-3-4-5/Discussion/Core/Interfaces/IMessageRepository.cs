using Core.Entities;

namespace Core.Interfaces;

public interface IMessageRepository
{
    Task<Message?> AddMessage(Message message);
    Task<Message?> GetMessage(long messageId);
    Task<Message?> RemoveMessage(long messageId);
    Task<Message?> UpdateMessage(Message message);
    Task<IEnumerable<Message?>?> GetAllMessages();
    Task<IEnumerable<Message?>?> GetMessagesByArticleId(long messageId);
}