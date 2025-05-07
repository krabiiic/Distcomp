namespace Discussion.DTO.Request;

public class PostRequestDTO
{
    public long Id { get; set; }
    
    public long ArticleId { get; set; }
    
    public string Content { get; set; }
}