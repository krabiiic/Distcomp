using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace TaskSQL.Models;

[Table("tbl_tag")]
[Index(nameof(Name), IsUnique = true)]
public class Tag
{
    public long id { get; set; }
    [MaxLength(32)] public string Name { get; set; } = string.Empty;
    public List<Tweet> Tweets { get; } = [];
}