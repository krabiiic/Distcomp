using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace publisher.Models
{
    [Table("tbl_articles")]
    [Index(nameof(Title), IsUnique = true)]
    public class Article
    {
        public long Id { get; set; }

        [ForeignKey("Creator")] public long? CreatorId { get; set; }

        [MaxLength(32)] public string Title { get; set; } = string.Empty;
        [MaxLength(2048)] public string Content { get; set; } = string.Empty;

        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public DateTime? Created { get; init; }

        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public DateTime? Modified { get; set; }

        public Creator? Creator { get; set; }
        public List<Tag> Tags { get; } = [];
    }
}
