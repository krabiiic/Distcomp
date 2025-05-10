using System;

namespace Infrastructure.Settings;

public class MongoOptions
{
    public string MongoConnectionString { get; set; } = null!;
    public string DatabaseName { get; set; } = null!;
    public string ReactionCollectionName { get; set; } = null!;
}
