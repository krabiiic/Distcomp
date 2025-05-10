using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core
{
    public class Story
    {
        [Column("id")]
        public long Id { get; set; } = 0;
        [Column("editor_id")]
        public long EditorId { get; set; } = 0;
        [Column("title")]
        public string Title { get; set; } = string.Empty;
        [Column("content")]
        public string Content { get; set; } = string.Empty;
        [Column("created")]
        public DateTime Created { get; set; } = DateTime.UtcNow;
        [Column("modified")]
        public DateTime Modified { get; set; } = DateTime.UtcNow;

        public Editor Editor { get; set; }
        public ICollection<StoryTag> StoryTags { get; set; }
    }
}
