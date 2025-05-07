using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;
using Core.Settings;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Driver;

namespace Core.Repositories.MongoDb;

public class MessageMongoDbRepository : IMessageRepository, IMongoDbRepository
{
    private readonly IMongoCollection<MessageMongoDb> _messages;
    
    public MessageMongoDbRepository(
        IOptions<MongoDbOptions> settings
    )
    {
        var mongoClient = new MongoClient(settings.Value.MongoConnectionString);

        var mongoDatabase = mongoClient.GetDatabase(settings.Value.DatabaseName);

        _messages = mongoDatabase.GetCollection<MessageMongoDb>(
            settings.Value.PostCollectionName
        );
        
        var indexKeysDefinition = Builders<MessageMongoDb>.IndexKeys.Ascending(p => p.Id);
        var indexModel = new CreateIndexModel<MessageMongoDb>(indexKeysDefinition, new CreateIndexOptions { Unique = true });
        _messages.Indexes.CreateOne(indexModel);   
    }
    
    public async Task<Message?> AddMessage(Message message)
    {
        var messageMongo = new MessageMongoDb(message);
        
        var newMongoId = ObjectId.GenerateNewId().ToString();
        messageMongo.MongoId = newMongoId;

        await _messages.InsertOneAsync(messageMongo);
        
        return messageMongo.ToMessage();
    }

    public async Task<Message?> GetMessage(long messageId)
    {
        
        var result = await _messages.Find(x => x.Id == messageId).FirstOrDefaultAsync();
        if (result == null)
            throw new NotFoundException("Id", messageId.ToString());
        return result.ToMessage();
    }

    public async Task<Message?> RemoveMessage(long messageId)
    {
        var message = await _messages.FindOneAndDeleteAsync(p => p.Id == messageId);
        if (message == null)
            throw new NotFoundException("Id", messageId.ToString());
        return message.ToMessage();
    }

    public async Task<Message?> UpdateMessage(Message message)
    {
        var messageMongo = new MessageMongoDb(message);
        var messageInDb = await _messages.Find(x => x.Id ==  messageMongo.Id).FirstOrDefaultAsync();
        if (messageInDb == null)
        {
            return null;
        }
        messageMongo.MongoId = messageInDb.MongoId;
        var result = await _messages.ReplaceOneAsync(p => p.Id == message.Id,  messageMongo);
        return result.IsAcknowledged && result.ModifiedCount > 0 ?  messageMongo.ToMessage() : null;
    }

    public async Task<IEnumerable<Message?>?> GetAllMessages()
    {
        var messages = await _messages.Find(_ => true).ToListAsync();
        if (messages == null)
            return new List<Message?>();
        return messages.Select(a=> a.ToMessage());
    }

    public async Task<IEnumerable<Message?>?> GetMessagesByArticleId(long articleId)
    {
        var messages = await _messages.Find(p => p.ArticleId == articleId).ToListAsync();
        return messages.Select(a=> a.ToMessage());
    }
}