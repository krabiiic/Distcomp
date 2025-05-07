namespace WebApplication1.Entity
{
    public class Note
    {
        public long Id { get; set; }
        public long ArticleId { get; set; }
        public string Content { get; set; } = string.Empty;

        public Article Article { get; set; } = null!;
    }
}
