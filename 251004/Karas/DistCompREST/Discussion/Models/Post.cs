using Cassandra.Mapping.Attributes;

namespace Discussion.Models;

[Table("tbl_post")]
public class Post : BaseModel
{
    [ClusteringKey]
    public long ArticleId { get; set; }
    
    public string Content { get; set; }
}