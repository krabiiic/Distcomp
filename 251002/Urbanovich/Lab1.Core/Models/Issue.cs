namespace Lab1.Core.Models
{
    public class Issue
    {
        public ulong CreatorId { get; } = 0;
        public string Title { get; } = string.Empty;
        public string Content { get; } = string.Empty;
        public DateTime Created { get; } = DateTime.MinValue;
        public DateTime Modified { get; } = DateTime.MinValue;
        private Issue(ulong creatorId, string title, string content) => (CreatorId, Title, Content) = (creatorId, title, content);
        static public Issue Construct(ulong creatorId, string title, string content)
        {
            return new Issue(creatorId, title, content);
        }

    }
}
