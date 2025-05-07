namespace DistComp_1.Models;

public class Author : BaseModel
{
    public string Login { get; set; }
    public string Password { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }

    public List<News> Stories { get; set; } = [];
}