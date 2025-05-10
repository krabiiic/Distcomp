namespace Lab3.Core.Models
{
    public class Message
    {
        public ulong IssueId { get; } = 0;
        public string Content { get; } = string.Empty;
        private Message(ulong issueId, string content) => (IssueId, Content) = (issueId, content);
        static public Message Construct(ulong issueId, string content)
        {
            return new Message(issueId, content);
        }
    }
}
