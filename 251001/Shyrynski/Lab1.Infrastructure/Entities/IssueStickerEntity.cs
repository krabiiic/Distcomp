namespace Lab1.Infrastructure.Entities
{
    public class IssueStickerEntity
    {
        public ulong Id { get; set; } = 0;
        public ulong IssueId { get; set; } = 0;
        public ulong StickerId { get; set; } = 0;
        public StickerEntity? Sticker { get; set; }
        public IssueEntity? Issue { get; set; }
    }
}
