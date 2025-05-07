using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace lab2_jpa.Models
{
    [Table("tbl_tag")]
    [Index(nameof(Name), IsUnique = true)]
    public class Tag
    {
        public long id { get; set; }
        [MaxLength(32)] public string Name { get; set; } = string.Empty;
        public List<Article> Articles { get; } = [];
    }
}
