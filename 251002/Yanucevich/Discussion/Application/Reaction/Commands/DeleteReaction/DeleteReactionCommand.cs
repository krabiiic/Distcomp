using Application.Abstractions;
using MongoDB.Bson;

namespace Application.Features.Reaction.Commands;

public record class DeleteReactionCommand(long id) : ICommand;
