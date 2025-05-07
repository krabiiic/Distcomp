using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Reaction : BaseModel
{
    [Required]
    [MinLength(2)]
    [MaxLength(2048)]
    [Column(TypeName = "text")]
    public string Content { get; set; }
    
    public long NewsId { get; set; }
    public virtual News News { get; set; }
}