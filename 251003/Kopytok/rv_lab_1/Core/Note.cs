using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core
{
    public class Note
    {
        [Column("id")]
        public long Id { get; set; } = 0;
        [Column("storyId")]
        public long StoryId { get; set; } = 0;
        [Column("content")]
        public String Content { get; set; } = string.Empty;
    }

    public enum NoteState
    {
        PENDING,
        APPROVE,
        DECLINE
    }

    public enum NoteAction
    {
        Get,    // GET
        GetAll, // GET ALL
        Create, // POST
        Update, // PUT
        Delete  // DELETE
    }

    public class KafkaMessage
    {
        public NoteAction Action { get; set; }
        public NoteState State { get; set; } = NoteState.PENDING;
        public long RequestId { get; set; }
        public Note Note { get; set; }
        public List<Note> Notes { get; set; }
    }
}
