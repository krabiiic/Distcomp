using MongoDB.Driver;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Linq;
using MyApp.Models;

namespace MyApp.Repositories
{
    public class DiscussionNoteRepository : IDiscussionNoteRepository
    {
        private readonly IMongoCollection<Note> _notes;

        public DiscussionNoteRepository(IMongoDatabase database)
        {
            _notes = database.GetCollection<Note>("tbl_note");
        }

        public async Task<Note> GetByIdAsync(int id)
        {
            return await _notes.Find(note => note.id == id).FirstOrDefaultAsync();
        }

        public async Task<IEnumerable<Note>> GetAllAsync()
        {
            return await _notes.Find(note => true).ToListAsync();
        }

        public async Task InsertAsync(Note note)
        {
            await _notes.InsertOneAsync(note);
        }

        public async Task UpdateAsync(Note note)
        {
            await _notes.ReplaceOneAsync(n => n.id == note.id, note);
        }

        public async Task DeleteAsync(int id)
        {
            await _notes.DeleteOneAsync(note => note.id == id);
        }
    }
}