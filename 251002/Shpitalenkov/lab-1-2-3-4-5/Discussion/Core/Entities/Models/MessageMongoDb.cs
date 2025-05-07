using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Core.Entities;

public class MessageMongoDb
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string MongoId { get; set; } = string.Empty;
    
    [BsonElement("id")]
    public long Id { get; set; }
    
    [BsonElement("country")]
    public string Country { get; set; } = string.Empty;
        
    [BsonElement("articleId")]
    public long ArticleId { get; set; }
        
    [BsonElement("content")]
    public string Content { get; set; } = string.Empty;

    public MessageMongoDb(Message message)
    {
        Id = message.Id;
        Content = message.Content;
        ArticleId = message.ArticleId;
    }

    public Message ToMessage()
    {
        return new Message()
        {
            Id = Id,
            Content = Content,
            ArticleId = ArticleId
        };
    }
}