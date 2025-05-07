using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface ILabelService
{
    Task<IEnumerable<LabelResponseDTO>> GetLabelsAsync();

    Task<LabelResponseDTO> GetLabelByIdAsync(long id);

    Task<LabelResponseDTO> CreateLabelAsync(LabelRequestDTO label);

    Task<LabelResponseDTO> UpdateLabelAsync(LabelRequestDTO label);

    Task DeleteLabelAsync(long id);
}