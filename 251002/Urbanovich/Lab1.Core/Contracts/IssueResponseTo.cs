namespace Lab1.Core.Contracts
{
    public record IssueResponseTo
    {
        public long id { get; set; } = 0;
        public long creatorId { get; set; } = 0;
        public string title { get; set; } = string.Empty;
        public string content { get; set; } = string.Empty;
        public DateTime created { get; set; } = DateTime.MinValue;
        public DateTime modified { get; set; } = DateTime.MinValue;
    }
}
