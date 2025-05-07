using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace lab2_jpa.Models
{
    [Table("tbl_article")]
    [Index(nameof(Title), IsUnique = true)]
    public class Article
    {
        public long id { get; set; }

        [ForeignKey("Creator")] public long? creator_id { get; set; }

        [MaxLength(32)] public string Title { get; set; } = string.Empty;
        [MaxLength(2048)] public string Content { get; set; } = string.Empty;

        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public DateTime? Created { get; init; }

        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public DateTime? Modified { get; set; }

        public Creator? Creator { get; set; }
        public List<Note> Notes { get; } = [];
        public List<Tag> Tags { get; } = [];
    }
}
