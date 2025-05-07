using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;

namespace Discussion.Services.Interfaces;

public interface IMessageService
{
    Task<IEnumerable<MessageResponseDTO>> GetMessagesAsync();

    Task<MessageResponseDTO> GetMessageByIdAsync(long id);

    Task<MessageResponseDTO> CreateMessageAsync(MessageRequestDTO message);

    Task<MessageResponseDTO> UpdateMessageAsync(MessageRequestDTO message);

    Task DeleteMessageAsync(long id);
}