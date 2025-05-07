using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients;
using Publisher.HttpClients.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class ReactionsController : ControllerBase
{
    private readonly IDiscussionClient _reactionClient;

    public ReactionsController(IDiscussionClient reactionClient)
    {
        _reactionClient = reactionClient;
    }

    [HttpGet]
    public async Task<IActionResult> GetReactions()
    {
        var reactions = await _reactionClient.GetReactionsAsync();
        return Ok(reactions);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetReactionById(long id)
    {
        var reaction = await _reactionClient.GetReactionByIdAsync(id);
        return Ok(reaction);
    }

    [HttpPost]
    public async Task<IActionResult> CreateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var createdReaction = await _reactionClient.CreateReactionAsync(reaction);
        return CreatedAtAction(nameof(CreateReaction), new { id = createdReaction.Id }, createdReaction);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var updatedReaction = await _reactionClient.UpdateReactionAsync(reaction);
        return Ok(updatedReaction);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReaction(long id)
    {
        await _reactionClient.DeleteReactionAsync(id);
        return NoContent();
    }
}