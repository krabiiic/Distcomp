using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core
{
    public class Tag
    {
        [Column("id")]
        public long Id { get; set; } = 0;
        [Column("name")]
        public String Name { get; set; } = string.Empty;
        public ICollection<StoryTag> StoryTags { get; set; }
    }
}
