using Application.Abstractions;
using Domain.Projections;
using MongoDB.Bson;

namespace Application.Features.Reaction.Queries;

public record class GetReactionByIdQuery(long id) : IQuery<ReactionProjection>;
