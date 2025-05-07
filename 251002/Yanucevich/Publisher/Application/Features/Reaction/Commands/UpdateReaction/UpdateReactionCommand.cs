namespace Application.Features.Reaction.Commands;

public record class UpdateReactionCommand(long id, long tweetId, string content);
