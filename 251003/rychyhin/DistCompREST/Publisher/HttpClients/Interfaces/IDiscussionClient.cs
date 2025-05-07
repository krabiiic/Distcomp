using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<NoteResponseDTO>?> GetNotesAsync();

    Task<NoteResponseDTO?> GetNoteByIdAsync(long id);

    Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO note);

    Task<NoteResponseDTO?> UpdateNoteAsync(long id, NoteRequestDTO note);

    Task DeleteNoteAsync(long id);
}