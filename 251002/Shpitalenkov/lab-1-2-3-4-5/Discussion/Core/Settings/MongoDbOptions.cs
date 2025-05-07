namespace Core.Settings;

public class MongoDbOptions
{
    public string MongoConnectionString { get; set; } = null!;
    public string DatabaseName { get; set; } = null!;
    public string PostCollectionName { get; set; } = null!;
}