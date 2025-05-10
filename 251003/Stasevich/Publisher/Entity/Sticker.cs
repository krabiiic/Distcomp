namespace WebApplication1.Entity
{
    public class Sticker
    {
        public long Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public List<Article> Articles { get; set; } = new();
    }
}
