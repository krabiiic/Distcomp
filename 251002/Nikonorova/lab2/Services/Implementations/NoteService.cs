using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.EntityFrameworkCore;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using lab2_jpa.Exceptions;
using lab2_jpa.Mappers;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.Models;
using System;

namespace lab2_jpa.Services.Implementations
{
    public sealed class NoteService(AppDbContext context) : INoteService
    {
        public async Task<NoteResponseTo> GetNoteById(long id)
        {
            var note = await context.Notes.FindAsync(id);
            if (note == null) throw new EntityNotFoundException($"Note with id = {id} doesn't exist.");

            return NoteMapper.Map(note);
        }

        public async Task<IEnumerable<NoteResponseTo>> GetNotes()
        {
            return NoteMapper.Map(await context.Notes.ToListAsync());
        }

        public async Task<NoteResponseTo> CreateNote(CreateNoteRequestTo createNoteRequestTo)
        {
            var note = NoteMapper.Map(createNoteRequestTo);
            await context.Notes.AddAsync(note);
            await context.SaveChangesAsync();
            return NoteMapper.Map(note);
        }

        public async Task DeleteNote(long id)
        {
            var note = await context.Notes.FindAsync(id);
            if (note == null) throw new EntityNotFoundException($"Note with id = {id} doesn't exist.");

            context.Notes.Remove(note);
            await context.SaveChangesAsync();
        }

        public async Task<NoteResponseTo> UpdateNote(UpdateNoteRequestTo modifiedNote)
        {
            var note = await context.Notes.FindAsync(modifiedNote.id);
            if (note == null) throw new EntityNotFoundException($"Note with id = {modifiedNote.id} doesn't exist.");

            context.Entry(note).State = EntityState.Modified;

            note.id = modifiedNote.id;
            note.Content = modifiedNote.Content;
            note.Articleid = modifiedNote.Articleid;

            await context.SaveChangesAsync();
            return NoteMapper.Map(note);
        }
    }
}
