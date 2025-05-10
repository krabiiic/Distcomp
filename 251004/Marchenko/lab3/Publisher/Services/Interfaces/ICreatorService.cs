using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface ICreatorService
{
    Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync();

    Task<CreatorResponseDTO> GetCreatorByIdAsync(long id);

    Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO Creator);

    Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO Creator);

    Task DeleteCreatorAsync(long id);
}