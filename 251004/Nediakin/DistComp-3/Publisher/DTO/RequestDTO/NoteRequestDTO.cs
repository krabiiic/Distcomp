namespace Publisher.DTO.RequestDTO;

public class NoteRequestDTO
{
    public long Id { get; set; }
    
    public long IssueId { get; set; }
    
    public string Content { get; set; }
}