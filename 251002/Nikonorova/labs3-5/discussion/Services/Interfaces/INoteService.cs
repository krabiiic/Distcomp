using discussion.DTO.Request;
using discussion.DTO.Response;

namespace discussion.Services.Interfaces
{
    public interface INoteService
    {
        Task<NoteResponseTo> GetNoteById(long id);
        Task<IEnumerable<NoteResponseTo>> GetNotes();
        Task<NoteResponseTo> CreateNote(NoteRequestTo noteRequestTo);
        Task DeleteNote(long id);
        Task<NoteResponseTo> UpdateNote(NoteRequestTo modifiedNote);
    }
}
