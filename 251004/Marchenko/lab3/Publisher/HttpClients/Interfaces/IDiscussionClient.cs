using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<MessageResponseDTO>?> GetMessagesAsync();

    Task<MessageResponseDTO?> GetMessageByIdAsync(long id);

    Task<MessageResponseDTO?> CreateMessageAsync(MessageRequestDTO post);

    Task<MessageResponseDTO?> UpdateMessageAsync(MessageRequestDTO post);

    Task DeleteMessageAsync(long id);
}