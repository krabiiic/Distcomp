namespace Lab3.Core.Models
{
    public class Post
    {
        public ulong NewsId { get; } = 0;
        public string Content { get; } = string.Empty;
        private Post(ulong newsId, string content) => (NewsId, Content) = (newsId, content);
        static public Post Construct(ulong newsId, string content) 
        {
            return new Post(newsId, content);
        }
    }
}
