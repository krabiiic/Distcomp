using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface ICreatorService
{
    Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync();

    Task<CreatorResponseDTO> GetCreatorByIdAsync(long id);

    Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO Creator);

    Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO Creator);

    Task DeleteCreatorAsync(long id);
}