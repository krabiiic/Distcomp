using System.ComponentModel.DataAnnotations.Schema;

namespace WebApplication1.Entity
{
    public class Article
    {
        public long Id { get; set; }

        [Column("user_id")]
        public long UserId { get; set; }
        public string Title { get; set; } = string.Empty;
        public string Content { get; set; } = string.Empty;
        public DateTime Created { get; set; } = DateTime.UtcNow;
        public DateTime Modified { get; set; } = DateTime.UtcNow;

        public User User { get; set; } = null!;
        public List<Note> Notes { get; set; } = new();
        public List<Sticker> Stickers { get; set; } = new();
    }
}
