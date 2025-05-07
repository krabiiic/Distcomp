using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Publisher.Models;

[Table("tbl_tags")]
[Index(nameof(Name), IsUnique = true)]
public class Tag
{
    public long Id { get; set; }
    [MaxLength(32)] public string Name { get; set; } = string.Empty;
    public List<Tweet> Tweets { get; } = [];
}