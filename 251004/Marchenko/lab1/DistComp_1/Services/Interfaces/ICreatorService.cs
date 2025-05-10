using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface ICreatorService
{
    Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync();

    Task<CreatorResponseDTO> GetCreatorByIdAsync(long id);

    Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO Creator);

    Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO Creator);

    Task DeleteCreatorAsync(long id);
}