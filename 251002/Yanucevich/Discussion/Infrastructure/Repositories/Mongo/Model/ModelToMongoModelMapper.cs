using System;
using Domain.Models;
using Domain.Projections;

public static class ModelToMongoModelMapper
{
    public static ReactionMongoModel ToReactionMongoModel(this ReactionModel model)
    {
        return new ReactionMongoModel
        {
            Id = model.Id,
            TweetId = model.TweetId,
            Content = model.Content,
        };
    }

    public static ReactionModel ToReactionModel(this ReactionMongoModel model)
    {
        return new ReactionModel
        {
            Id = model.Id,
            TweetId = model.TweetId,
            Content = model.Content,
        };
    }
}
