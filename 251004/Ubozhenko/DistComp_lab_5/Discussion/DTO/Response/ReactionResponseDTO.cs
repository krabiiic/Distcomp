namespace Discussion.DTO.Response;

public class ReactionResponseDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}