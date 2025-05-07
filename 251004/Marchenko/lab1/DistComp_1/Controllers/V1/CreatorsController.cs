using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class CreatorsController : ControllerBase
{
    private readonly ICreatorService _CreatorService;

    public CreatorsController(ICreatorService CreatorService)
    {
        _CreatorService = CreatorService;
    }

    [HttpGet]
    public async Task<IActionResult> GetCreators()
    {
        var Creators = await _CreatorService.GetCreatorsAsync();
        return Ok(Creators);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetCreatorById(long id)
    {
        var Creator = await _CreatorService.GetCreatorByIdAsync(id);
        return Ok(Creator);
    }

    [HttpPost]
    public async Task<IActionResult> CreateCreator([FromBody] CreatorRequestDTO Creator)
    {
        var createdCreator = await _CreatorService.CreateCreatorAsync(Creator);
        return CreatedAtAction(nameof(CreateCreator), new { id = createdCreator.Id }, createdCreator);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateCreator([FromBody] CreatorRequestDTO Creator)
    {
        var updatedCreator = await _CreatorService.UpdateCreatorAsync(Creator);
        return Ok(updatedCreator);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteCreator(long id)
    {
        await _CreatorService.DeleteCreatorAsync(id);
        return NoContent();
    }
}