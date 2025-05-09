using System.ComponentModel.DataAnnotations.Schema;

namespace DistComp.Models;

public class Creator : BaseModel
{
    [Column("login")]
    public string Login { get; set; }
    [Column("password")]
    public string Password { get; set; }
    [Column("firstname")]
    public string Firstname { get; set; }
    [Column("lastname")]
    public string Lastname { get; set; }

    public List<Issue> Issues { get; set; } = [];
}