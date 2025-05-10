using Cassandra.Mapping.Attributes;

namespace Discussion.Models;

[Table("tbl_reaction")]
public class Reaction : BaseModel
{
    [ClusteringKey]
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}