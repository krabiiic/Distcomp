using System.ComponentModel.DataAnnotations.Schema;

namespace LAB5.Models;

public class Issue
{
    public int id { get; set; }
    public int writer_id { get; set; }
    public string title { get; set; }
    public string content { get; set; }
    public DateTime created { get; set; }  // Добавлено обратно
    public DateTime modified { get; set; } // Добавлено обратно
    public Writer writer { get; set; }
    public List<Post> posts { get; set; } = new();
    public List<Sticker> stickers { get; set; } = new();
}