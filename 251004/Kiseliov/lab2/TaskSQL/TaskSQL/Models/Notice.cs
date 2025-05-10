using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TaskSQL.Models;

[Table("tbl_notice")]
public class Notice
{
    public long id { get; set; }

    [ForeignKey("Tweet")] public long? Tweetid { get; set; }

    [MaxLength(2048)] public string Content { get; set; } = string.Empty;
    public Tweet? Tweet { get; set; }
}