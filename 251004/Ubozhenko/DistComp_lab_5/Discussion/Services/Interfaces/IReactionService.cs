using Discussion.DTO.Request;
using Discussion.DTO.Response;

namespace Discussion.Services.Interfaces;

public interface IReactionService
{
    Task<IEnumerable<ReactionResponseDTO>> GetReactionsAsync();

    Task<ReactionResponseDTO> GetReactionByIdAsync(long id);

    Task<ReactionResponseDTO> CreateReactionAsync(ReactionRequestDTO reaction);

    Task<ReactionResponseDTO> UpdateReactionAsync(ReactionRequestDTO reaction);

    Task DeleteReactionAsync(long id);
}