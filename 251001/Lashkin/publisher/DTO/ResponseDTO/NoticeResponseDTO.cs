using Publisher.Models;

namespace Publisher.DTO.ResponseDTO;

public class NoticeResponseDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    public News News { get; set; }
    
    public string Content { get; set; }
}