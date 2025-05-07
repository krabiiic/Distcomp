namespace Discussion.DTO.Request;

public class ReactionRequestDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}