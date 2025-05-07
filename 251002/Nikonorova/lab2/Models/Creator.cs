using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace lab2_jpa.Models
{
    [Table("tbl_creator")]
    [Index(nameof(Login), IsUnique = true)]
    public class Creator
    {
        public long id { get; set; }

        [MaxLength(64)] public string Login { get; set; } = string.Empty;

        [MaxLength(128)] public string Password { get; set; } = string.Empty;

        [MaxLength(64)] public string FirstName { get; set; } = string.Empty;

        [MaxLength(64)] public string LastName { get; set; } = string.Empty;
        public List<Article> Articles { get; } = new();
    }
}
