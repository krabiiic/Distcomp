using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Message : BaseModel
{
    [Required]
    [MinLength(2)]
    [MaxLength(2048)]
    [Column(TypeName = "text")]
    public string Content { get; set; }
    
    public long IssueId { get; set; }
    public Issue Issue { get; set; }
}