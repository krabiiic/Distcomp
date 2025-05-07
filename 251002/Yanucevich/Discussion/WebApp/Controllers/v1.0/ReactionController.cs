using System;
using System.Reflection.Metadata;
using Application.Features.Reaction.Commands;
using Application.Features.Reaction.Queries;
using Asp.Versioning;
using Discussion.Abstractions;
using Domain.Projections;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using Shared.Domain;

namespace Discussion.Controllers;

[Route("reactions")]
[ApiVersion("1.0")]
public class ReactionController : MediatrController
{
    public ReactionController(IMediator mediator)
        : base(mediator) { }

    [HttpPost]
    public async Task<IActionResult> CreateReaction(CreateReactionCommand command)
    {
        var result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status201Created, result.Value);
    }

    [HttpGet]
    public async Task<IActionResult> GetReactions()
    {
        Result<IEnumerable<ReactionProjection>>? result = await _mediator.Send(
            new GetReactionsQuery()
        );

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetReactionByIdQuery(long id)
    {
        var result = await _mediator.Send(new GetReactionByIdQuery(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateReaction(UpdateReactionCommand command)
    {
        Result<ReactionProjection> result = await _mediator.Send(command);

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status200OK, result.Value);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReaction(long id)
    {
        Result result = await _mediator.Send(new DeleteReactionCommand(id));

        if (!result.IsSuccess)
        {
            return HandleFailure(result);
        }

        return StatusCode(StatusCodes.Status204NoContent);
    }
}
