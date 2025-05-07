using Application.Abstractions;
using Domain.Projections;
using MongoDB.Bson;

namespace Application.Features.Reaction.Commands;

public record class UpdateReactionCommand(long id, long tweetId, string content)
    : ICommand<ReactionProjection>;
