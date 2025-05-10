namespace LAB2.DTOs;

public class IssueRequestTo
{
    public int id { get; set;  }
    public string Title { get; set; }
    public string Content { get; set; }
    public int WriterId { get; set; }
    
    public List<string> Stickers { get; set; } = new();
}