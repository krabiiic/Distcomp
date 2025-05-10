using System;
using Domain.Models;
using Domain.Repositories;
using Infrastructure.Errors;
using Infrastructure.Repositories.Interfaces;
using Infrastructure.Settings;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Driver;
using Shared.Domain;

namespace Infrastructure.Repositories.Mongo;

public class ReactionRepositoryMongo : IReactionRepository, IMongoRepository
{
    private readonly IMongoCollection<ReactionMongoModel> _reactionCollection;

    public ReactionRepositoryMongo(IOptions<MongoOptions> settings)
    {
        var mongoClient = new MongoClient(settings.Value.MongoConnectionString);

        var mongoDatabase = mongoClient.GetDatabase(settings.Value.DatabaseName);

        _reactionCollection = mongoDatabase.GetCollection<ReactionMongoModel>(
            settings.Value.ReactionCollectionName
        );

        var indexKeysDefinition = Builders<ReactionMongoModel>.IndexKeys.Ascending(p => p.Id);
        var indexModel = new CreateIndexModel<ReactionMongoModel>(
            indexKeysDefinition,
            new CreateIndexOptions { Unique = true }
        );
        _reactionCollection.Indexes.CreateOne(indexModel);
    }

    public async Task<Result<ReactionModel>> CreateReaction(ReactionModel reaction)
    {
        var reactionMongo = reaction.ToReactionMongoModel();
        if (reactionMongo.Id == -1)
        {
            var maxReaction = await _reactionCollection
                .Find(_ => true)
                .SortByDescending(p => p.Id)
                .Limit(1)
                .FirstOrDefaultAsync();
            reactionMongo.Id = (maxReaction?.Id ?? 0) + 1;
        }
        reactionMongo.MongoId = ObjectId.GenerateNewId();

        await _reactionCollection.InsertOneAsync(reactionMongo);

        return reactionMongo.ToReactionModel();

        /*catch (NpgsqlException ex) when (ex.SqlState == "23503")*/
        /*{*/
        /*    return Result.Failure<ReactionModel>(ReactionErrors.TweetForReactionNotFoundError);*/
        /*}*/
        /*catch (NpgsqlException ex)*/
        /*{*/
        /*    return Result.Failure<ReactionModel>(Error.DatabaseError);*/
        /*}*/
        /**/
        /*return Result.Failure<ReactionModel>(Error.Unknown);*/
    }

    public async Task<Result> DeleteReaction(long id)
    {
        var reaction = await _reactionCollection.FindOneAndDeleteAsync(r => r.Id == id);
        return Result.Success();
        /*catch (NpgsqlException ex)*/
        /*{*/
        /*    return Result.Failure(Error.DatabaseError);*/
        /*}*/
        /**/
        /*return result == 0*/
        /*    ? Result.Failure(ReactionErrors.ReactionNotFoundError)*/
        /*    : Result.Success();*/
    }

    public async Task<Result<ReactionModel>> GetReactionById(long id)
    {
        var reaction = await _reactionCollection.Find(x => x.Id == id).FirstOrDefaultAsync();
        if (reaction is null)
        {
            return Result.Failure<ReactionModel>(ReactionErrors.ReactionNotFoundError);
        }
        return reaction.ToReactionModel();

        /*catch (NpgsqlException ex)*/
        /* {*/
        /*     return Result.Failure<ReactionModel>(Error.DatabaseError);*/
        /* }*/
    }

    public async Task<Result<IEnumerable<ReactionModel>>> GetReactions()
    {
        var reactions = await _reactionCollection.Find(_ => true).ToListAsync();
        return Result.Success(reactions.Select(r => r.ToReactionModel()));

        /*catch (NpgsqlException ex)*/
        /*{*/
        /*    return Result.Failure<IEnumerable<ReactionModel>>(Error.DatabaseError);*/
        /*}*/
    }

    public async Task<Result<ReactionModel>> UpdateReaction(ReactionModel reaction)
    {
        var reactionMongo = await _reactionCollection
            .Find(x => x.Id == reaction.Id)
            .FirstOrDefaultAsync();
        if (reactionMongo is null)
        {
            return Result.Failure<ReactionModel>(ReactionErrors.ReactionNotFoundError);
        }
        var newReactionMongo = reaction.ToReactionMongoModel();
        newReactionMongo.MongoId = reactionMongo.MongoId;

        var result = await _reactionCollection.ReplaceOneAsync(
            r => r.Id == reaction.Id,
            newReactionMongo
        );
        if (result.IsAcknowledged && result.ModifiedCount > 0)
        {
            return newReactionMongo.ToReactionModel();
        }
        else
        {
            return Result.Failure<ReactionModel>(Error.Unknown);
        }

        /*catch (NpgsqlException ex) when (ex.SqlState == "23503")*/
        /*{*/
        /*    return Result.Failure<ReactionModel>(ReactionErrors.TweetForReactionNotFoundError);*/
        /*}*/
        /*catch (NpgsqlException ex)*/
        /*{*/
        /*    return Result.Failure<ReactionModel>(Error.DatabaseError);*/
        /*}*/
    }
}
