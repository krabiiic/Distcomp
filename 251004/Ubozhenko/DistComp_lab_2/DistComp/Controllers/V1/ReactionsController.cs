using DistComp.DTO.RequestDTO;
using DistComp.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class ReactionsController : ControllerBase
{
    private readonly IReactionsService _reactionsService;

    public ReactionsController(IReactionsService reactionsService)
    {
        _reactionsService = reactionsService;
    }

    [HttpGet]
    public async Task<IActionResult> GetReactions()
    {
        var reactions = await _reactionsService.GetReactionsAsync();
        return Ok(reactions);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetReactionById(long id)
    {
        var reaction = await _reactionsService.GetReactionByIdAsync(id);
        return Ok(reaction);
    }

    [HttpPost]
    public async Task<IActionResult> CreateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var createdReaction = await _reactionsService.CreateReactionAsync(reaction);
        return CreatedAtAction(nameof(CreateReaction), new { id = createdReaction.Id }, createdReaction);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateReaction([FromBody] ReactionRequestDTO reaction)
    {
        var updatedReaction = await _reactionsService.UpdateReactionAsync(reaction);
        return Ok(updatedReaction);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReaction(long id)
    {
        await _reactionsService.DeleteReactionAsync(id);
        return NoContent();
    }
}