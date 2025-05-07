using publisher.DTO.Request;
using publisher.DTO.Response;

namespace publisher.Services.Interfaces
{
    public interface INoteService
    {
        Task<NoteResponseTo> GetNoteById(long id);
        Task<IEnumerable<NoteResponseTo>> GetNotes();
        Task<NoteResponseTo> CreateNote(CreateNoteRequestTo createNoteRequestTo, string country);
        Task DeleteNote(long id);
        Task<NoteResponseTo> UpdateNote(UpdateNoteRequestTo modifiedNote, string country);
    }
}
