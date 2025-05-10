namespace Lab1.Core.Models
{
    public class News
    {
        public ulong UserId { get; } = 0;
        public string Title { get; } = string.Empty;
        public string Content { get; } = string.Empty;
        public DateTime Created { get; } = DateTime.MinValue;
        public DateTime Modified { get; } = DateTime.MinValue;
        private News(ulong userId, string title, string content) => (UserId, Title, Content) = (userId, title, content);
        static public News Construct(ulong userId, string title, string content)
        {
            return new News(userId, title, content);
        }

    }
}
