using MyApp.Models;
using MyApp.Repositories;
using System.Collections.Generic;
using System.Threading.Tasks;

public interface IDiscussionNoteService
{
    Task<Note> GetNoteByIdAsync(int id);
    Task<IEnumerable<Note>> GetAllNotesAsync();
    Task<Note> CreateNoteAsync(Note note);
    Task<bool> UpdateNoteAsync(Note note);
    Task<bool> DeleteNoteAsync(int id);
}

public class DiscussionNoteService : IDiscussionNoteService
{
    private readonly IDiscussionNoteRepository _repository;

    public DiscussionNoteService(IDiscussionNoteRepository repository)
    {
        _repository = repository;
    }

    public async Task<Note> GetNoteByIdAsync(int id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<IEnumerable<Note>> GetAllNotesAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<Note> CreateNoteAsync(Note note)
    {
        await _repository.InsertAsync(note);
        return note;
    }

    public async Task<bool> UpdateNoteAsync(Note note)
    {
        await _repository.UpdateAsync(note);
        return true;
    }

    public async Task<bool> DeleteNoteAsync(int id)
    {
        await _repository.DeleteAsync(id);
        return true;
    }
}