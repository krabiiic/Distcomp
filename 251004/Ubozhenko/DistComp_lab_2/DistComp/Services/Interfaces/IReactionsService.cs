using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IReactionsService
{
    Task<IEnumerable<ReactionResponseDTO>> GetReactionsAsync();

    Task<ReactionResponseDTO> GetReactionByIdAsync(long id);

    Task<ReactionResponseDTO> CreateReactionAsync(ReactionRequestDTO reaction);

    Task<ReactionResponseDTO> UpdateReactionAsync(ReactionRequestDTO reaction);

    Task DeleteReactionAsync(long id);
}