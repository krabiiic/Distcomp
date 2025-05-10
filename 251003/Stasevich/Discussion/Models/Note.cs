using Cassandra.Mapping.Attributes;
using System;

namespace Discussion.Models
{
    [Table("tbl_note")]
    public class Note
    {
        [PartitionKey]
        [Column("country")]
        public string Country { get; set; } = string.Empty;

        [ClusteringKey(1)]
        [Column("articleid")]
        public long ArticleId { get; set; }

        [ClusteringKey(2)]
        [Column("id")]
        public long Id { get; set; }

        [Column("content")]
        public string Content { get; set; } = string.Empty;

        [Column("created")]
        public DateTime Created { get; set; } = DateTime.UtcNow;

        [Column("modified")]
        public DateTime Modified { get; set; } = DateTime.UtcNow;
    }
}
