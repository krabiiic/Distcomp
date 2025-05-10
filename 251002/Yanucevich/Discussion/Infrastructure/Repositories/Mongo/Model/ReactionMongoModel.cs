using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Driver;

public class ReactionMongoModel
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public ObjectId? MongoId { get; set; }

    public long Id { get; set; }
    public long TweetId { get; set; }
    public string Content { get; set; }
}
