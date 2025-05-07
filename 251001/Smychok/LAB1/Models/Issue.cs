namespace LAB1.Models;

public class Issue
{
    public int Id { get; set; }
    public string Title { get; set; }
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }
    public int WriterId { get; set; }
    public Writer Writer { get; set; }
    public ICollection<Sticker> Stickers { get; set; } = new List<Sticker>();
    public ICollection<Post> Posts { get; set; } = new List<Post>();
}