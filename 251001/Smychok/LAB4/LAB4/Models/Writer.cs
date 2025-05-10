using System.ComponentModel.DataAnnotations.Schema;

namespace LAB4.Models;

public class Writer
{
    public int id { get; set; }
    
    [Column(TypeName = "varchar(64)")]
    public string login { get; set; }
    
    [Column(TypeName = "varchar(128)")]
    public string password { get; set; }
    
    [Column(TypeName = "varchar(64)")]
    public string firstname { get; set; }
    
    [Column(TypeName = "varchar(64)")]
    public string lastname { get; set; }
    
    public List<Issue> issues { get; set; } = new();
}