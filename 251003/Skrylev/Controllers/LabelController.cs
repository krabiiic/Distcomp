using Microsoft.AspNetCore.Mvc;
using MyApp.Models;

[ApiController]
[Route("api/v1.0/labels")]
public class LabelsController : ControllerBase
{
    private readonly ILabelService _labelService;

    public LabelsController(ILabelService labelService, ILogger<LabelsController> logger)
    {
        _labelService = labelService;
    }

    [HttpPost]
    public async Task<IActionResult> CreateLabel([FromBody] LabelRequestTo labelDto)
    {
        try
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var createdLabel = await _labelService.CreateLabelAsync(labelDto);
            return CreatedAtAction(nameof(GetLabelById), new { id = createdLabel.Id }, createdLabel);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(ex.Message);
        }
        catch (Exception)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, "An error occurred while creating the Label.");
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetLabelById(int id)
    {
        try
        {
            var label = await _labelService.GetLabelByIdAsync(id);
            if (label == null)
            {
                return NotFound();
            }
            return Ok(label);
        }
        catch (Exception)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, "An error occurred while retrieving the Label.");
        }
    }

    [HttpGet]
    public async Task<IActionResult> GetAllLabels()
    {
        try
        {
            var labels = await _labelService.GetAllLabelsAsync();
            return Ok(labels);
        }
        catch (Exception)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, "An error occurred while retrieving Label.");
        }
    }

    [HttpPut]
    public async Task<IActionResult> UpdateLabel([FromBody] Label labelDto)
    {
        try
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var updatedLabel = await _labelService.UpdateLabelAsync(labelDto);
            if (updatedLabel == null)
            {
                return NotFound();
            }
            return Ok(updatedLabel);
        }
        catch (Exception)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, "An error occurred while updating the Label.");
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteLabel(int id)
    {
        try
        {
            bool deleted = await _labelService.DeleteLabelAsync(id);
            if (!deleted)
            {
                return NotFound();
            }
            return NoContent();
        }
        catch (Exception)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, "An error occurred while deleting the Label.");
        }
    }
}
