using WebApplication1.DTO;

namespace WebApplication1.Service
{
    public interface IRemoteNoteService
    {
        Task<NoteResponseTo> CreateNoteAsync(NoteRequestTo dto);
        Task<NoteResponseTo> GetNoteByIdAsync(long id);
        Task<List<NoteResponseTo>> GetAllNotesAsync();
        Task<NoteResponseTo> UpdateNoteAsync(NoteRequestTo dto);
        Task DeleteNoteAsync(string id);
    }
}
