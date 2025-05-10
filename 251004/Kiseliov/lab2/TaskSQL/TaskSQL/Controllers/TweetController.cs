using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;
using TaskSQL.Services.Interfaces;

namespace TaskSQL.Controllers;

[Route("api/v{version:apiVersion}/tweets")]
[ApiVersion("1.0")]
[ApiController]
public class TweetController(ITweetService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Tweet>> GetTweet(long id)
    {
        return Ok(await service.GetTweetById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Tweet>>> GetTweets()
    {
        return Ok(await service.GetTweets());
    }

    [HttpPost]
    public async Task<ActionResult<TweetResponseTo>> CreateTweet(CreateTweetRequestTo createTweetRequestTo)
    {
        var addedTweet = await service.CreateTweet(createTweetRequestTo);
        return CreatedAtAction(nameof(GetTweet), new { id = addedTweet.id }, addedTweet);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteTweet(long id)
    {
        await service.DeleteTweet(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<TweetResponseTo>> UpdateTweet(UpdateTweetRequestTo updateTweetRequestTo)
    {
        return Ok(await service.UpdateTweet(updateTweetRequestTo));
    }
}