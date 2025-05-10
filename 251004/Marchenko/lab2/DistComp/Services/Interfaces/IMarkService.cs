using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IMarkService
{
    Task<IEnumerable<MarkResponseDTO>> GetMarksAsync();

    Task<MarkResponseDTO> GetMarkByIdAsync(long id);

    Task<MarkResponseDTO> CreateMarkAsync(MarkRequestDTO mark);

    Task<MarkResponseDTO> UpdateMarkAsync(MarkRequestDTO mark);

    Task DeleteMarkAsync(long id);
}