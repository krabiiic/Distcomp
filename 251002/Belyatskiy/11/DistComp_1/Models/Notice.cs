namespace DistComp_1.Models;

public class Message : BaseModel
{
    public long ArticleId { get; set; }
    public Article Article { get; set; }
    
    public string Content { get; set; }
}