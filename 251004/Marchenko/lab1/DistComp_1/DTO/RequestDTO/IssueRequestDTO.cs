namespace DistComp_1.DTO.RequestDTO;

public class IssueRequestDTO
{
    public long Id { get; set; }
    public string Title { get; set; }
    
    public long CreatorId { get; set; }

    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

}