using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface ICreatorService
{
    Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync();

    Task<CreatorResponseDTO> GetCreatorByIdAsync(long id);

    Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO creator);

    Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO creator);

    Task DeleteCreatorAsync(long id);
}