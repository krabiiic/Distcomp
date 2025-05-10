using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTO.requests
{
    public class NoteRequestTo
    {
        public long StoryId { get; set; }

        [StringLength(2048, MinimumLength = 2)]
        public string Content { get; set; } = string.Empty;
    }
}
