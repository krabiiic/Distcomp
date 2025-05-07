using static System.Net.Mime.MediaTypeNames;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace publisher.Models
{
    [Table("tbl_tags")]
    [Index(nameof(Name), IsUnique = true)]
    public class Tag
    {
        public long Id { get; set; }
        [MaxLength(32)] public string Name { get; set; } = string.Empty;
        public List<Article> Articles { get; } = [];
    }
}
