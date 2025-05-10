// Models/Post.cs
using Cassandra.Mapping.Attributes;

namespace CassandraPosts.Models;

[Table("tbl_post")]
public class Post
{
    [Column("post_id")]
    public Guid Id { get; set; }

    [Column("issue_id")]
    public int IssueId { get; set; }

    [Column("content")]
    public string Content { get; set; }

    [PartitionKey]
    [Column("country")]
    public string Country { get; set; } = "default"; // Для совместимости с тестами
}