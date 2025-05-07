using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class LabelsController : ControllerBase
{
    private readonly ILabelService _tagService;

    public LabelsController(ILabelService tagService)
    {
        _tagService = tagService;
    }

    [HttpGet]
    public async Task<IActionResult> GetLabels()
    {
        var tags = await _tagService.GetLabelsAsync();
        return Ok(tags);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetLabelById(long id)
    {
        var tag = await _tagService.GetLabelByIdAsync(id);
        return Ok(tag);
    }

    [HttpPost]
    public async Task<IActionResult> CreateLabel([FromBody] LabelRequestDTO label)
    {
        var createdLabel = await _tagService.CreateLabelAsync(label);
        return CreatedAtAction(nameof(CreateLabel), new { id = createdLabel.Id }, createdLabel);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateLabel([FromBody] LabelRequestDTO label)
    {
        var updatedLabel = await _tagService.UpdateLabelAsync(label);
        return Ok(updatedLabel);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteLabel(long id)
    {
        await _tagService.DeleteLabelAsync(id);
        return NoContent();
    }
}