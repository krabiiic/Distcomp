using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class ReactionsController : ControllerBase
{
    private readonly IReactionService _reactionService;

    public ReactionsController(IReactionService reactionService)
    {
        _reactionService = reactionService;
    }

    [HttpGet]
    public async Task<IActionResult> GetReactions()
    {
        var reactions = await _reactionService.GetReactionsAsync();
        return Ok(reactions);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetReactionById(long id)
    {
        var reaction = await _reactionService.GetReactionByIdAsync(id);
        return Ok(reaction);
    }

    [HttpPost]
    public async Task<IActionResult> CreateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var createdReaction = await _reactionService.CreateReactionAsync(reaction);
        return CreatedAtAction(nameof(CreateReaction), new { id = createdReaction.Id }, createdReaction);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var updatedReaction = await _reactionService.UpdateReactionAsync(reaction);
        return Ok(updatedReaction);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReaction(long id)
    {
        await _reactionService.DeleteReactionAsync(id);
        return NoContent();
    }
}