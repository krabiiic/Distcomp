using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<ReactionResponseDTO>?> GetReactionsAsync();

    Task<ReactionResponseDTO?> GetReactionByIdAsync(long id);

    Task<ReactionResponseDTO?> CreateReactionAsync(ReactionRequestDTO post);

    Task<ReactionResponseDTO?> UpdateReactionAsync(ReactionRequestDTO post);

    Task DeleteReactionAsync(long id);
}