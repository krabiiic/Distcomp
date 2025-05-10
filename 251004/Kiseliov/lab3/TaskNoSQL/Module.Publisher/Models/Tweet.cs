using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Publisher.Models;

[Table("tbl_tweets")]
[Index(nameof(Title), IsUnique = true)]
public class Tweet
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