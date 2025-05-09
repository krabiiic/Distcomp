using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Issue : BaseModel
{
    [Column("title")]
    public string Title { get; set; }
    [Column("creator_id")]
    public long CreatorId { get; set; }
    public Creator Creator { get; set; }

    public List<Note> Notes { get; set; } = [];
    
    [Column("content")]
    public string Content { get; set; }
    [Column("created")]
    public DateTime Created { get; set; }
    [Column("modified")]
    public DateTime Modified { get; set; }

    public List<Sticker> Stickers { get; set; } = [];
}