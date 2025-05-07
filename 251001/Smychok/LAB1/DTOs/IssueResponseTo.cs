namespace LAB1.DTOs;

public class IssueResponseTo
{
    public int Id { get; set; }
    public string Title { get; set; }
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }
    public int WriterId { get; set; }
}