using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface ICreatorService
{
    Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync();

    Task<CreatorResponseDTO> GetCreatorByIdAsync(long id);

    Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO creator);

    Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO creator);

    Task DeleteCreatorAsync(long id);
}