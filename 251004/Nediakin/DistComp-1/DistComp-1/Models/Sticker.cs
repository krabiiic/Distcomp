namespace DistComp_1.Models;

public class Sticker : BaseModel
{
    public string Name { get; set; }

    public List<Issue> Issues { get; set; } = [];
}