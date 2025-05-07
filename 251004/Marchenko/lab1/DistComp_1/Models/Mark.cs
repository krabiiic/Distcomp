namespace DistComp_1.Models;

public class Mark : BaseModel
{
    public string Name { get; set; }

    public List<Issue> Issues { get; set; } = [];
}