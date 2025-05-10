using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Note : BaseModel
{
    [Column("issueId")]
    public long IssueId { get; set; }
    public Issue Issue { get; set; }
    [Column("content")]
    public string Content { get; set; }
}