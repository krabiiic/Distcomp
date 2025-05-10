using Publisher.Models;

namespace Publisher.DTO.ResponseDTO;

public class PostResponseDTO
{
    public long Id { get; set; }
    
    public long ArticleId { get; set; }
    public Article Article { get; set; }
    
    public string Content { get; set; }
}