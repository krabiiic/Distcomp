using Discussion.Models;
using Discussion.Repositories;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Discussion.Services
{
    public class NoteService : INoteService
    {
        private readonly INoteRepository _noteRepository;
        public NoteService(INoteRepository noteRepository)
        {
            _noteRepository = noteRepository;
        }

        public async Task<IEnumerable<Note>> GetAllNotesAsync()
        {
            return await _noteRepository.GetAllAsync();
        }

        public async Task<Note?> GetNoteByIdAsync(long id)
        {
            return await _noteRepository.GetByIdAsync(id);
        }

        public async Task<Note> CreateNoteAsync(Note note)
        {
            if (note.Id == 0)
            {
                note.Id = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
            }
            await _noteRepository.CreateAsync(note);
            return note;
        }

        public async Task<Note> UpdateNoteAsync(long id, Note note)
        {
            var existing = await GetNoteByIdAsync(id);
            if (existing == null)
                throw new KeyNotFoundException();
            note.Modified = DateTime.UtcNow;
            await _noteRepository.UpdateAsync(id, note);
            return note;
        }

        public async Task DeleteNoteAsync(long id)
        {
            await _noteRepository.DeleteAsync(id);
        }
    }
}
