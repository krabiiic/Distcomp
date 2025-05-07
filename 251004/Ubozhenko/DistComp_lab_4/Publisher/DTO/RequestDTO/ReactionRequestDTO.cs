namespace Publisher.DTO.RequestDTO;

public class ReactionRequestDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}