using Discussion.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Discussion.Services
{
    public interface INoteService
    {
        Task<IEnumerable<Note>> GetAllNotesAsync();
        Task<Note?> GetNoteByIdAsync(long id);
        Task<Note> CreateNoteAsync(Note note);
        Task<Note> UpdateNoteAsync(long id, Note note);
        Task DeleteNoteAsync(long id);
    }
}
