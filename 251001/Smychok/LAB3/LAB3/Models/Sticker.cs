namespace LAB2.Models;

public class Sticker
{
    public int id { get; set; }
    public string name { get; set; }
    public ICollection<Issue> issues { get; set; } = new List<Issue>();
}