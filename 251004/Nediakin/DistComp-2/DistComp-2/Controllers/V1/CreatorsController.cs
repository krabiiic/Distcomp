using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class CreatorsController : ControllerBase
{
    private readonly ICreatorService _creatorService;

    public CreatorsController(ICreatorService creatorService)
    {
        _creatorService = creatorService;
    }

    [HttpGet]
    public async Task<IActionResult> GetCreators()
    {
        var creators = await _creatorService.GetCreatorsAsync();
        return Ok(creators);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetCreatorById(long id)
    {
        var creator = await _creatorService.GetCreatorByIdAsync(id);
        return Ok(creator);
    }

    [HttpPost]
    public async Task<IActionResult> CreateCreator([FromBody] CreatorRequestDTO creator)
    {
        var createdCreator = await _creatorService.CreateCreatorAsync(creator);
        return CreatedAtAction(nameof(CreateCreator), new { id = createdCreator.Id }, createdCreator);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateCreator([FromBody] CreatorRequestDTO creator)
    {
        var updatedCreator = await _creatorService.UpdateCreatorAsync(creator);
        return Ok(updatedCreator);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteCreator(long id)
    {
        await _creatorService.DeleteCreatorAsync(id);
        return NoContent();
    }
}