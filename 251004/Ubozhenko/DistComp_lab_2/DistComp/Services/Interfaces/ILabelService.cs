using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using LabelResponseDTO = DistComp.DTO.ResponseDTO.LabelResponseDTO;

namespace DistComp.Services.Interfaces;

public interface ILabelService
{
    Task<IEnumerable<LabelResponseDTO>> GetLabelsAsync();

    Task<LabelResponseDTO> GetLabelByIdAsync(long id);

    Task<LabelResponseDTO> CreateLabelsync(DTO.RequestDTO.LabelResponseDTO label);

    Task<LabelResponseDTO> UpdateLabelAsync(DTO.RequestDTO.LabelResponseDTO label);

    Task DeleteLabelAsync(long id);
}