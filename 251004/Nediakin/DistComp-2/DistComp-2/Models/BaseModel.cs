using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public abstract class BaseModel
{
    [Column("id")]
    public long Id { get; set; }
}