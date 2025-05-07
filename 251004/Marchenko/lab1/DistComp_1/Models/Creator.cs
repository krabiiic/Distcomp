namespace DistComp_1.Models;

public class Creator : BaseModel
{
    public string Login { get; set; }
    public string Password { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }

    public List<Issue> Issues { get; set; } = [];
}