using Core;
using DTO.requests;
using DTO.responces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Discussion.abstractions
{
    public interface INoteService
    {
        Task<NoteResponseTo> AddNoteRespAsync(NoteRequestTo request);
        Task<NoteResponseTo> AddNoteAsync(Note note);
        Task<bool> DeleteNoteAsync(long id);
        Task<IEnumerable<NoteResponseTo>> GetNotesByStoryIdAsync(long storyId);
        Task<IEnumerable<NoteResponseTo>> GetAllAsync();
        Task<NoteResponseTo?> GetByIdAsync(long id);
        Task<bool> UpdateAsync(long id, NoteRequestTo request);
    }
}
