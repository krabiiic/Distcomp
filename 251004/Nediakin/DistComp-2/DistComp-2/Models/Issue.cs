namespace DistComp_1.Models;

public class Issue : BaseModel
{
    public string Title { get; set; }
    
    public long CreatorId { get; set; }
    public Creator Creator { get; set; }

    public List<Note> Notes { get; set; } = [];
    
    public string Content { get; set; }
    public DateTime Created { get; set; }
    public DateTime Modified { get; set; }

    public List<Sticker> Stickers { get; set; } = [];
}