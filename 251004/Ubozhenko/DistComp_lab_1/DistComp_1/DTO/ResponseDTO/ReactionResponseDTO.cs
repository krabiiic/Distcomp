using DistComp_1.Models;

namespace DistComp_1.DTO.ResponseDTO;

public class ReactionResponseDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    public News News { get; set; }
    
    public string Content { get; set; }
}