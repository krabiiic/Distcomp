using DistComp.Models;

namespace DistComp.DTO.ResponseDTO;

public class NewsRequestDTO
{
    public long Id { get; set; }
    public string Title { get; set; }
    
    public long AuthorId { get; set; }
    public Author Author { get; set; }

    public List<Reaction> Reactions { get; set; }
    
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

    public List<Label> Labels { get; set; } = [];

}