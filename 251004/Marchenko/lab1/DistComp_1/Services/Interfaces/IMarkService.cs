using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IMarkService
{
    Task<IEnumerable<MarkResponseDTO>> GetMarksAsync();

    Task<MarkResponseDTO> GetMarkByIdAsync(long id);

    Task<MarkResponseDTO> CreateMarkAsync(MarkRequestDTO mark);

    Task<MarkResponseDTO> UpdateMarkAsync(MarkRequestDTO mark);

    Task DeleteMarkAsync(long id);
}