namespace DistComp_1.Models;

public class Label : BaseModel
{
    public string Name { get; set; }

    public List<News> News { get; set; } = [];
}