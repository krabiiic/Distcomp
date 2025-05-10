namespace LAB1.Models;

public class Sticker
{
    public int Id { get; set; }
    public string Name { get; set; }
    public ICollection<Issue> Issues { get; set; } = new List<Issue>();
}