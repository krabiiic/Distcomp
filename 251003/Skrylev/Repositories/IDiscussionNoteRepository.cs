using MyApp.Models;

namespace MyApp.Repositories
{
    public interface IDiscussionNoteRepository
    {
        Task<Note> GetByIdAsync(int id);
        Task<IEnumerable<Note>> GetAllAsync();
        Task InsertAsync(Note note);
        Task UpdateAsync(Note note);
        Task DeleteAsync(int id);
    }
}
