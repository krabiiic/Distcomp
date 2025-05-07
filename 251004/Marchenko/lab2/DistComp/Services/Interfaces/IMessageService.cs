using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IMessageService
{
    Task<IEnumerable<MessageResponseDTO>> GetMessagesAsync();

    Task<MessageResponseDTO> GetMessageByIdAsync(long id);

    Task<MessageResponseDTO> CreateMessageAsync(MessageRequestDTO message);

    Task<MessageResponseDTO> UpdateMessageAsync(MessageRequestDTO message);

    Task DeleteMessageAsync(long id);
}