namespace Publisher.DTO.RequestDTO;

public class ArticleRequestDTO
{
    public long Id { get; set; }
    public string Title { get; set; }
    
    public long EditorId { get; set; }

    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

}