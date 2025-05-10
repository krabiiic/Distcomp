using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IMessageService
{
    Task<IEnumerable<MessageResponseDTO>> GetMessagesAsync();

    Task<MessageResponseDTO> GetMessageByIdAsync(long id);

    Task<MessageResponseDTO> CreateMessageAsync(MessageRequestDTO Message);

    Task<MessageResponseDTO> UpdateMessageAsync(MessageRequestDTO Message);

    Task DeleteMessageAsync(long id);
}