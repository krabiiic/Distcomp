using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class MarksController : ControllerBase
{
    private readonly IMarkService _MarkService;

    public MarksController(IMarkService MarkService)
    {
        _MarkService = MarkService;
    }

    [HttpGet]
    public async Task<IActionResult> GetMarks()
    {
        var Marks = await _MarkService.GetMarksAsync();
        return Ok(Marks);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMarkById(long id)
    {
        var Mark = await _MarkService.GetMarkByIdAsync(id);
        return Ok(Mark);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMark([FromBody] MarkRequestDTO Mark)
    {
        var createdMark = await _MarkService.CreateMarkAsync(Mark);
        return CreatedAtAction(nameof(CreateMark), new { id = createdMark.Id }, createdMark);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMark([FromBody] MarkRequestDTO Mark)
    {
        var updatedMark = await _MarkService.UpdateMarkAsync(Mark);
        return Ok(updatedMark);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMark(long id)
    {
        await _MarkService.DeleteMarkAsync(id);
        return NoContent();
    }
}