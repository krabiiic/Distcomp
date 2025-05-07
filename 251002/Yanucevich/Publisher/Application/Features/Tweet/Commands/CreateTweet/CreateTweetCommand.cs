using Application.Abstractions;
using Domain.Models;
using Domain.Projections;

namespace Application.Features.Tweet.Commands;

public record class CreateTweetCommand(
    long userId,
    string title,
    string content,
    List<string>? markers
) : ICommand<TweetProjection>;

