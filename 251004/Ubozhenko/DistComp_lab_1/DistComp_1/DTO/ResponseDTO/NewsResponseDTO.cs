using DistComp_1.Models;

namespace DistComp_1.DTO.ResponseDTO;

public class NewsResponseDTO
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