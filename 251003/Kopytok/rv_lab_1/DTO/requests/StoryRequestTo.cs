using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DTO.requests
{
    public class StoryRequestTo
    {
        public long EditorId { get; set; }

        [StringLength(64, MinimumLength = 2)]
        public string Title { get; set; } = string.Empty;

        [StringLength(2048, MinimumLength = 4)]
        public string Content { get; set; } = string.Empty;

        public List<String> Tags { get; set; } = new List<String>();
    }

}
