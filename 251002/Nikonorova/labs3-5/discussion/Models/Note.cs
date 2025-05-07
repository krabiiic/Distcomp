using Cassandra.Mapping.Attributes;


namespace discussion.Models
{
    [Table("tbl_notes", AllowFiltering = true)]
    public class Note
    {
        [PartitionKey] public string Country { get; set; } = string.Empty;
        [ClusteringKey(0)] public long ArticleId { get; set; }
        [ClusteringKey(1)] public long Id { get; set; }
        public string Content { get; set; } = string.Empty ;
    }
}
