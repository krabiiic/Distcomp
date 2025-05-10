using Discussion.DTO.Request;
using Discussion.DTO.Response;

namespace Discussion.Services.Interfaces;

public interface INoteService
{
    Task<IEnumerable<NoteResponseDTO>> GetNotesAsync();

    Task<NoteResponseDTO> GetNoteByIdAsync(long id);

    Task<NoteResponseDTO> CreateNoteAsync(NoteRequestDTO note);

    Task<NoteResponseDTO> UpdateNoteAsync(long id, NoteRequestDTO note);

    Task DeleteNoteAsync(long id);
}