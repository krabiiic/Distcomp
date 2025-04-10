using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface INoteService
{
    Task<IEnumerable<NoteResponseDTO>> GetNotesAsync();

    Task<NoteResponseDTO> GetNoteByIdAsync(long id);

    Task<NoteResponseDTO> CreateNoteAsync(NoteRequestDTO note);

    Task<NoteResponseDTO> UpdateNoteAsync(NoteRequestDTO note);

    Task DeleteNoteAsync(long id);
}