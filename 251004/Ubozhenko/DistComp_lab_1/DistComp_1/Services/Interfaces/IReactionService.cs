using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IReactionService
{
    Task<IEnumerable<ReactionResponseDTO>> GetReactionsAsync();

    Task<ReactionResponseDTO> GetReactionByIdAsync(long id);

    Task<ReactionResponseDTO> CreateReactionAsync(ReactionRequestDTO reaction);

    Task<ReactionResponseDTO> UpdateReactionAsync(ReactionRequestDTO reaction);

    Task DeleteReactionAsync(long id);
}