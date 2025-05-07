using Discussion.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Discussion.Repositories
{
    public interface INoteRepository
    {
        Task<IEnumerable<Note>> GetAllAsync();
        Task<Note?> GetByIdAsync(long id);
        Task CreateAsync(Note note);
        Task UpdateAsync(long id, Note note);
        Task DeleteAsync(long id);
    }
}
