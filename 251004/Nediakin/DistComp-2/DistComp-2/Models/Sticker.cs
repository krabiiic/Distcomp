using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Sticker : BaseModel
{
    [Column("name")]
    public string Name { get; set; }
    
    public List<Issue> Issues { get; set; } = [];
}