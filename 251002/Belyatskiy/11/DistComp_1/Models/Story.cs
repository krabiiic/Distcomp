namespace DistComp_1.Models;

public class Article : BaseModel
{
    public string Title { get; set; }
    
    public long UserId { get; set; }
    public User User { get; set; }

    public List<Message> Messages { get; set; } = [];
    
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

    public List<Mark> Marks { get; set; } = [];
}