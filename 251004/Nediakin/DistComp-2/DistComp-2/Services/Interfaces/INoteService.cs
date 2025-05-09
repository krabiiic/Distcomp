using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface INoteService
{
    Task<IEnumerable<NoteResponseDTO>> GetNotesAsync();

    Task<NoteResponseDTO> GetNoteByIdAsync(long id);

    Task<NoteResponseDTO> CreateNoteAsync(NoteRequestDTO note);

    Task<NoteResponseDTO> UpdateNoteAsync(NoteRequestDTO note);

    Task DeleteNoteAsync(long id);
}