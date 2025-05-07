namespace Discussion.DTO.Request;

public class NoteRequestDTO
{
    public long Id { get; set; }
    
    public long NewsId { get; set; }
    
    public string Content { get; set; }
}