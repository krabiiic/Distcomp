using Cassandra.Mapping.Attributes;

namespace Discussion.Models;

[Table("tbl_note")]
public class Note : BaseModel
{
    [ClusteringKey]
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}