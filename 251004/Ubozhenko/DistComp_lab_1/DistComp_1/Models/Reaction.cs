namespace DistComp_1.Models;

public class Reaction : BaseModel
{
    public long NewsId { get; set; }
    public News News { get; set; }
    
    public string Content { get; set; }
}