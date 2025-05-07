namespace LAB1.Models;

public class Post
{
    public int Id { get; set; }
    public string Content { get; set; }
    public int IssueId { get; set; }
    public Issue Issue { get; set; }
}