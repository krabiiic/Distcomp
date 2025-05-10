namespace DistComp_1.Models;

public class Note : BaseModel
{
    public long IssueId { get; set; }
    public Issue Issue { get; set; }
    
    public string Content { get; set; }
}