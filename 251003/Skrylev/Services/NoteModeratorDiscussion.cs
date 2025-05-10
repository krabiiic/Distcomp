using MyApp.Models;

namespace MyApp.Services
{
    public class NoteModerator
    {
        private static readonly string[] StopWords = { "badword", "forbidden" };
        public Note Moderate(Note note)
        {
            if (StopWords.Any(sw => note.Content.Contains(sw, StringComparison.OrdinalIgnoreCase)))
                note.State = NoteState.DECLINE;
            else
                note.State = NoteState.APPROVE;
            return note;
        }
    }
}
