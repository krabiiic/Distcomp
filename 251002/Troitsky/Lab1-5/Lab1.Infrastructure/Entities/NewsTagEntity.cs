namespace Lab1.Infrastructure.Entities
{
    public class NewsTagEntity
    {
        public ulong Id { get; set; } = 0;
        public ulong NewsId { get; set; } = 0;
        public ulong TagId { get; set; } = 0;
        public TagEntity? Tag { get; set; }
        public NewsEntity? News { get; set; }
    }
}
