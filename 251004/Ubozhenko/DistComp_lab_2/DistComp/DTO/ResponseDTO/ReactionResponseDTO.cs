using DistComp.Models;

namespace DistComp.DTO.ResponseDTO;

public class ReactionResponseDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    public News News { get; set; }
    
    public string Content { get; set; }
}