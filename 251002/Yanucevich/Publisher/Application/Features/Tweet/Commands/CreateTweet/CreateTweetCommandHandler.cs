using System;
using System.Net.Http.Headers;
using Application.Abstractions;
using Domain.Mappers;
using Domain.Models;
using Domain.Projections;
using Domain.Repositories;
using Shared.Domain;

namespace Application.Features.Tweet.Commands;

public class CreateTweetCommandHandler : ICommandHandler<CreateTweetCommand, TweetProjection>
{
    private readonly ITweetRepository _tweetRepository;
    private readonly IMarkerRepository _markerRepository;

    public CreateTweetCommandHandler(
        ITweetRepository tweetRepository,
        IMarkerRepository markerRepository
    )
    {
        _tweetRepository = tweetRepository;
        _markerRepository = markerRepository;
    }

    public async Task<Result<TweetProjection>> Handle(
        CreateTweetCommand request,
        CancellationToken cancellationToken
    )
    {
        // create tweet
        var resultTweet = await _tweetRepository.CreateTweet(
            new TweetModel
            {
                UserId = request.userId,
                Title = request.title,
                Content = request.content,
            }
        );

        if (!resultTweet.IsSuccess)
        {
            return Result.Failure<TweetProjection>(resultTweet.Error);
        }

        if (request.markers is not null)
        {
            // create markers
            var createdMarkers = await _markerRepository.GetMarkersCreateIfDoNotExist(
                request.markers.Select(m => new MarkerModel { Name = m })
            );
            if (!createdMarkers.IsSuccess)
            {
                return Result.Failure<TweetProjection>(createdMarkers.Error);
            }

            // attach markers
            var markersAdded = await _tweetRepository.AddMarkersToTweet(
                resultTweet.Value.Id,
                createdMarkers.Value.Select(m => m.Id)
            );

            if (!markersAdded.IsSuccess)
            {
                return Result.Failure<TweetProjection>(markersAdded.Error);
            }
        }

        return resultTweet.Value.ToTweetProjection();
    }
}
