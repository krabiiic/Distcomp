using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface IEditorService
{
    Task<IEnumerable<EditorResponseDTO>> GetEditorsAsync();

    Task<EditorResponseDTO> GetEditorByIdAsync(long id);

    Task<EditorResponseDTO> CreateEditorAsync(EditorRequestDTO editor);

    Task<EditorResponseDTO> UpdateEditorAsync(EditorRequestDTO editor);

    Task DeleteEditorAsync(long id);
}