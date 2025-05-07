using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface ILabelService
{
    Task<IEnumerable<LabelResponseDTO>> GetLabelsAsync();

    Task<LabelResponseDTO> GetLabelByIdAsync(long id);

    Task<LabelResponseDTO> CreateLabelAsync(LabelRequestDTO label);

    Task<LabelResponseDTO> UpdateLabelAsync(LabelRequestDTO label);

    Task DeleteLabelAsync(long id);
}