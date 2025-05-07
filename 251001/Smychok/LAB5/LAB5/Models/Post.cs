namespace LAB5.Models;

public class Post
{
    public int id { get; set; }
    public string content { get; set; }
    public int issueId { get; set; }
    public Issue issue { get; set; }
}