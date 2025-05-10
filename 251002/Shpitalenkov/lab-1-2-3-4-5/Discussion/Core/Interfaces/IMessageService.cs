using Core.DTO;
using Core.DTO;

namespace Core.Interfaces;

public interface IMessageService
{
    public Task<MessageResponseToGetById?> CreateMessage(MessageRequestToCreate model);
    public Task<IEnumerable<MessageResponseToGetById?>?> GetMessages(MessageRequestToGetAll request);
    public Task<IEnumerable<MessageResponseToGetById?>?> GetMessagesByArticleId(MessageRequestToGetByArticleId request);
    public Task<MessageResponseToGetById?> GetMessageById(MessageRequestToGetById request);
    public Task<MessageResponseToGetById?> UpdateMessage(MessageRequestToFullUpdate model);
    public Task<MessageResponseToGetById?> DeleteMessage(MessageRequestToDeleteById request);
}