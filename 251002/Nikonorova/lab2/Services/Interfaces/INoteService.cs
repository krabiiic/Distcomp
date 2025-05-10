using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Services.Interfaces
{
    public interface INoteService
    {
        Task<NoteResponseTo> GetNoteById(long id);
        Task<IEnumerable<NoteResponseTo>> GetNotes();
        Task<NoteResponseTo> CreateNote(CreateNoteRequestTo createNoteRequestTo);
        Task DeleteNote(long id);
        Task<NoteResponseTo> UpdateNote(UpdateNoteRequestTo modifiedNote);
    }
}
