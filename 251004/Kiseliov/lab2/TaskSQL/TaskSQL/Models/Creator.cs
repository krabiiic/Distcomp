using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace TaskSQL.Models;

[Table("tbl_creator")]
[Index(nameof(Login), IsUnique = true)]
public class Creator
{
    public long id { get; set; }

    [MaxLength(64)] public string Login { get; set; } = string.Empty;

    [MaxLength(128)] public string Password { get; set; } = string.Empty;

    [MaxLength(64)] public string FirstName { get; set; } = string.Empty;

    [MaxLength(64)] public string LastName { get; set; } = string.Empty;
    public List<Tweet> Tweets { get; } = new();
}