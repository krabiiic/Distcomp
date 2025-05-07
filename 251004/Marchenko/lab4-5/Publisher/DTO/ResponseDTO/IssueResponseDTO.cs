using Publisher.Models;

namespace Publisher.DTO.ResponseDTO;

public class IssueResponseDTO
{
    public long Id { get; set; }
    public string Title { get; set; }
    
    public long CreatorId { get; set; }
    public Creator Creator { get; set; }
    
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

    public List<Mark> Marks { get; set; } = [];

}