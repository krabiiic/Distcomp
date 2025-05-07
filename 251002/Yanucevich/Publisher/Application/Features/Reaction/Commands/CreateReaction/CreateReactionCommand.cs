namespace Application.Features.Reaction.Commands;

public record class CreateReactionCommand(long tweetId, string content);
