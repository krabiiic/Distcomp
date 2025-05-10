using System;
using Application.Features.Tweet.Commands;
using Application.Features.Tweet.Queries;
using Asp.Versioning;
using Domain.Projections;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using SocialNet.Abstractions;

namespace SocialNet.Controllers;

[Route("tweets")]
[ApiVersion("1.0")]
public class TweetController : MediatrController
{
    private readonly IRedisCacheService _redis;
    private readonly string _cache_key_prefix = "tweet";
    private readonly TimeSpan _cache_expiration = TimeSpan.FromSeconds(10);

    public TweetController(IMediator mediator, IRedisCacheService redis)
        : base(mediator)
    {
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateTweet(CreateTweetCommand command)
    {
        var result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status201Created, result.Value);
    }

    [HttpGet]
    public async Task<IActionResult> GetTweets()
    {
        var result = await _mediator.Send(new GetTweetsQuery());

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetTweetById(long id)
    {
        var cached_result = await _redis.GetCacheValueAsync<TweetProjection>(
            $"{_cache_key_prefix}:{id}"
        );

        if (cached_result is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cached_result);
        }

        var result = await _mediator.Send(new GetTweetByIdQuery(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        await _redis.SetCacheValueAsync<TweetProjection>(
            $"{_cache_key_prefix}:{id}",
            result.Value,
            _cache_expiration
        );

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateTweet(UpdateTweetCommand command)
    {
        var result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        var redisKey = $"{_cache_key_prefix}:{result.Value.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteTweet(long id)
    {
        var result = await _mediator.Send(new DeleteTweetCommand(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        var redisKey = $"{_cache_key_prefix}:{id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status204NoContent);
    }
}
