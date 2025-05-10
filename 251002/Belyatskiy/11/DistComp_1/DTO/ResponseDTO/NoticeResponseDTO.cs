using DistComp_1.Models;

namespace DistComp_1.DTO.ResponseDTO;

public class MessageResponseDTO
{
    public long Id { get; set; }
    
    public long ArticleId { get; set; }
    public Article Article { get; set; }
    
    public string Content { get; set; }
}