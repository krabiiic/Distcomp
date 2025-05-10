using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<NoteResponseDTO>?> GetNotesAsync();

    Task<NoteResponseDTO?> GetNoteByIdAsync(long id);

    Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO post);

    Task<NoteResponseDTO?> UpdateNoteAsync(NoteRequestDTO post);

    Task DeleteNoteAsync(long id);
}