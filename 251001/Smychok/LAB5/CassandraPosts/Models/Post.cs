using Cassandra.Mapping;
using Cassandra.Mapping.Attributes;
using Microsoft.EntityFrameworkCore;

namespace CassandraPosts.Models;

[Table("tbl_post", Keyspace = "distcomp")]
public class Post
{
    [Column("id")]
    public Guid id { get; set; }

    [ClusteringKey(0)]
    [Column("issue_id")]
    public int IssueId { get; set; }

    [Column("content")]
    public string Content { get; set; }

    [Column("country")]
    public string Country { get; set; } = "default";

    [Column("state")]
    public string State { get; set; } = "PENDING";
}