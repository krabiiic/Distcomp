using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Mark : BaseModel
{
    [Required]
    [MinLength(2)]
    [MaxLength(32)]
    [Column(name:"name", TypeName = "text")]
    public string Name { get; set; }

    public List<Issue> Issues { get; set; } = [];
}