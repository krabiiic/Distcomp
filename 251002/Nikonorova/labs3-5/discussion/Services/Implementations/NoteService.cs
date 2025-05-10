using discussion.DTO.Request;
using discussion.DTO.Response;
using discussion.Exceptions;
using discussion.Models;
using discussion.Mappers;
using discussion.Storage;
using discussion.Services.Interfaces;
using Cassandra.Data.Linq;

namespace discussion.Services.Implementations
{

    public sealed class NoteService(CDBContext context, ILogger<NoteService> logger) : INoteService
    {
        public async Task<NoteResponseTo> GetNoteById(long id)
        {
            Note? note = await context.Notes.FirstOrDefault(p => p.Id == id).ExecuteAsync();
            if (note == null) throw new EntityNotFoundException($"Note with id = {id} doesn't exist.");

            logger.LogInformation("Note with id = {Id} was found", id);
            return note.ToResponse();
        }

        public async Task<IEnumerable<NoteResponseTo>> GetNotes()
        {
            logger.LogInformation("Getting all notes");
            return (await context.Notes.ExecuteAsync()).ToResponse();
        }

        public async Task<NoteResponseTo> CreateNote(NoteRequestTo noteRequestTo)
        {
            Note note = noteRequestTo.ToEntity();
            await context.Notes.Insert(note).ExecuteAsync();
            logger.LogInformation("Note with id = {Id} was created", note.Id);
            return note.ToResponse();
        }

        public async Task DeleteNote(long id)
        {
            Note? note = await context.Notes.FirstOrDefault(p => p.Id == id).ExecuteAsync();
            if (note == null) throw new EntityNotFoundException($"Note with id = {id} doesn't exist.");

            await context.Notes.Where(p => p.Country == note.Country && p.Id == note.Id).Delete().ExecuteAsync();
            logger.LogInformation("Note with id = {Id} was deleted", id);
        }

        /*
        public async Task<NoteResponseTo> UpdateNote(NoteRequestTo modifiedNote)
        {
            Note? note = await context.Notes.FirstOrDefault(p => p.Id == modifiedNote.Id).ExecuteAsync();
            if (note == null) throw new EntityNotFoundException($"Note with id = {modifiedNote.Id} doesn't exist.");

            await context.Notes.Where(p => p.Country == note.Country && p.ArticleId == note.ArticleId && p.Id == note.Id)
                .Select(p => new Note
                {
                    Content = modifiedNote.Content
                })
                .Update()
                .ExecuteAsync();

            return modifiedNote.ToEntity().ToResponse();
        }
        */

        public async Task<NoteResponseTo> UpdateNote(NoteRequestTo modifiedNote)
        {
            Note? existing = await context.Notes.FirstOrDefault(p => p.Id == modifiedNote.Id).ExecuteAsync();
            if (existing == null)
                throw new EntityNotFoundException($"Note with id = {modifiedNote.Id} doesn't exist.");

            await context.Notes
                .Where(p => p.Country == modifiedNote.Country && p.ArticleId == modifiedNote.ArticleId && p.Id == modifiedNote.Id)
                .Select(p => new Note { Content = modifiedNote.Content })
                .Update()
                .ExecuteAsync();

            // Вернём "обновлённую" сущность
            return new Note
            {
                Id = modifiedNote.Id,
                ArticleId = modifiedNote.ArticleId,
                Country = modifiedNote.Country,
                Content = modifiedNote.Content
            }.ToResponse();
        }

    }
}
