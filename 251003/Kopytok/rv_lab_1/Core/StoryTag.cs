using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core
{
    public class StoryTag
    {
        [Column("id")]
        public long Id { get; set; } = 0;
        [Column("storyId")]
        public long StoryId { get; set; } = 0;
        [Column("tagId")]
        public long TagId { get; set; } = 0;

        public Story Story { get; set; }
        public Tag Tag { get; set; }
    }    
}
