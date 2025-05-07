using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace lab2_jpa.Models
{
    [Table("tbl_note")]
    public class Note
    {
        public long id { get; set; }

        [ForeignKey("Article")] public long? Articleid { get; set; }

        [MaxLength(2048)] public string Content { get; set; } = string.Empty;
        public Article? Article { get; set; }
    }
}
