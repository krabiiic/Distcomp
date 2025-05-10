using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using Riok.Mapperly.Abstractions;
using lab2_jpa.Models;

namespace lab2_jpa.Mappers
{
    [Mapper]
    public static partial class NoteMapper
    {
        public static partial Note Map(UpdateNoteRequestTo updateNoteRequestTo);
        public static partial Note Map(CreateNoteRequestTo createNoteRequestTo);
        public static partial NoteResponseTo Map(Note note);
        public static partial IEnumerable<NoteResponseTo> Map(IEnumerable<Note> notes);

        public static partial IEnumerable<Note> Map(
            IEnumerable<UpdateNoteRequestTo> noteRequestTos);
    }
}
