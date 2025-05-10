using Domain.Entities;
using Domain.Exceptions;
using Domain.Repositories;
using Infrastructure.Settings;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Driver;

namespace Infrastructure.Repositories.MongoDb;

public class PostMongoDbRepository : IPostRepository, IMongoDbRepository
{
    private readonly IMongoCollection<PostMongoDb> _posts;
    
    public PostMongoDbRepository(
        IOptions<MongoDbOptions> settings
    )
    {
        var mongoClient = new MongoClient(settings.Value.MongoConnectionString);

        var mongoDatabase = mongoClient.GetDatabase(settings.Value.DatabaseName);

        _posts = mongoDatabase.GetCollection<PostMongoDb>(
            settings.Value.PostCollectionName
        );
        
        var indexKeysDefinition = Builders<PostMongoDb>.IndexKeys.Ascending(p => p.Id);
        var indexModel = new CreateIndexModel<PostMongoDb>(indexKeysDefinition, new CreateIndexOptions { Unique = true });
        _posts.Indexes.CreateOne(indexModel);   
    }
    
    public async Task<Post?> AddPost(Post post)
    {
        var postMongo = new PostMongoDb(post);
        
        //var maxPost = await _posts.Find(_ => true).SortByDescending(p => p.Id).Limit(1).FirstOrDefaultAsync();
        //postMongo.Id = (maxPost?.Id ?? 0) + 1;
        
        var newMongoId = ObjectId.GenerateNewId().ToString();
        postMongo.MongoId = newMongoId;

        await _posts.InsertOneAsync(postMongo);
        
        return postMongo.ToPost();
    }

    public async Task<Post?> GetPost(long postId)
    {
        
        var result = await _posts.Find(x => x.Id == postId).FirstOrDefaultAsync();
        if (result == null)
            throw new NotFoundException("Id", postId.ToString());
        return result.ToPost();
    }

    public async Task<Post?> RemovePost(long postId)
    {
        var post = await _posts.FindOneAndDeleteAsync(p => p.Id == postId);
        if (post == null)
            throw new NotFoundException("Id", postId.ToString());
        return post.ToPost();
    }

    public async Task<Post?> UpdatePost(Post post)
    {
        var postMongo = new PostMongoDb(post);
        var postInDb = await _posts.Find(x => x.Id ==  postMongo.Id).FirstOrDefaultAsync();
        if (postInDb == null)
        {
            return null;
        }
        postMongo.MongoId = postInDb.MongoId;
        var result = await _posts.ReplaceOneAsync(p => p.Id == post.Id,  postMongo);
        return result.IsAcknowledged && result.ModifiedCount > 0 ?  postMongo.ToPost() : null;
    }

    public async Task<IEnumerable<Post?>?> GetAllPosts()
    {
        var posts = await _posts.Find(_ => true).ToListAsync();
        if (posts == null)
            return new List<Post?>();
        return posts.Select(a=> a.ToPost());
    }

    public async Task<IEnumerable<Post?>?> GetPostsByNewsId(long newsId)
    {
        var posts = await _posts.Find(p => p.NewsId == newsId).ToListAsync();
        return posts.Select(a=> a.ToPost());
    }
}