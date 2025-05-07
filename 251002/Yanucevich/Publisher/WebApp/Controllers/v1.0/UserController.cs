using Application.Features.User.Commands;
using Application.Features.User.Queries;
using Asp.Versioning;
using Domain.Projections;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Routing;
using SocialNet.Abstractions;

namespace SocialNet.Controllers;

[Route("users")]
[ApiVersion("1.0")]
public class UserController : MediatrController
{
    private readonly IRedisCacheService _redis;
    private readonly string _cache_key_prefix = "user";
    private readonly TimeSpan _cache_expiration = TimeSpan.FromSeconds(10);

    public UserController(IMediator mediator, IRedisCacheService redis)
        : base(mediator)
    {
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateUser(CreateUserCommand command)
    {
        var result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status201Created, result.Value);
    }

    [HttpGet]
    public async Task<IActionResult> GetUsers()
    {
        var result = await _mediator.Send(new GetUsersQuery());

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetUserById(long id)
    {
        var cached_result = await _redis.GetCacheValueAsync<UserProjection>(
            $"{_cache_key_prefix}:{id}"
        );

        var result = await _mediator.Send(new GetUserByIdQuery((id)));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        await _redis.SetCacheValueAsync<UserProjection>(
            $"{_cache_key_prefix}:{id}",
            result.Value,
            _cache_expiration
        );

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateUser(UpdateUserCommand command)
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
    public async Task<IActionResult> DeleteUser(long id)
    {
        var result = await _mediator.Send(new DeleteUserCommand(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        var redisKey = $"{_cache_key_prefix}:{id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return StatusCode(StatusCodes.Status204NoContent);
    }
}
