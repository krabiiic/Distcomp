namespace LAB2.Models;

public class IssueSticker
{
    public int issueId { get; set; }
    public int stickerId { get; set; }
    public Issue issue { get; set; } = null!;
    public Sticker sticker { get; set; } = null!;
}