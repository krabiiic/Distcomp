using discussion.DTO.Request;
using discussion.Models;
using discussion.DTO.Response;
using Riok.Mapperly.Abstractions;

namespace discussion.Mappers
{
    [Mapper]
    public static partial class NoteMapper
    {
        public static partial Note ToEntity(this NoteRequestTo noteRequestTo);
        public static partial NoteResponseTo ToResponse(this Note note);
        public static partial IEnumerable<NoteResponseTo> ToResponse(this IEnumerable<Note> notes);

        public static Note UpdateEntity(this Note note, NoteRequestTo updateNoteRequestTo) => new()
        {
            Id = note.Id,
            ArticleId = updateNoteRequestTo.ArticleId,
            Content = updateNoteRequestTo?.Content,
            Country = note.Country
        };
    }
}
