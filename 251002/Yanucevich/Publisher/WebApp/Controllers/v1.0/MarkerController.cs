using Application.Features.Marker.Commands;
using Application.Features.Marker.Queries;
using Asp.Versioning;
using Domain.Projections;
using MediatR;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Shared.Domain;
using SocialNet.Abstractions;

namespace SocialNet.Controllers;

[Route("markers")]
[ApiVersion("1.0")]
public class MarkerController : MediatrController
{
    private readonly ILogger<MarkerController> _logger;

    private readonly IRedisCacheService _redis;
    private readonly string _cache_key_prefix = "marker";
    private readonly TimeSpan _cache_expiration = TimeSpan.FromSeconds(10);

    public MarkerController(
        IMediator mediator,
        ILogger<MarkerController> logger,
        IRedisCacheService redis
    )
        : base(mediator)
    {
        _logger = logger;
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateMarker(CreateMarkerCommand command)
    {
        var result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status201Created, result.Value);
    }

    [HttpGet]
    public async Task<IActionResult> GetMarkers()
    {
        var result = await _mediator.Send(new GetMarkersQuery());

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMarkerById(long id)
    {
        var cached_result = await _redis.GetCacheValueAsync<MarkerProjection>(
            $"{_cache_key_prefix}:{id}"
        );

        if (cached_result is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cached_result);
        }

        var result = await _mediator.Send(new GetMarkerByIdQuery(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        await _redis.SetCacheValueAsync<MarkerProjection>(
            $"{_cache_key_prefix}:{id}",
            result.Value,
            _cache_expiration
        );

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMarker(UpdateMarkerCommand command)
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
    public async Task<IActionResult> DeleteMarker(long id)
    {
        var result = await _mediator.Send(new DeleteMarkerCommand(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        var redisKey = $"{_cache_key_prefix}:{id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status204NoContent);
    }
}
