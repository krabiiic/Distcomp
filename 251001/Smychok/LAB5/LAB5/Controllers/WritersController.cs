using LAB5.Repositories;

namespace LAB5.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using DTOs;
using Services;

[Route("api/v1.0/[controller]")]
[ApiController]
public class WritersController : ControllerBase
{
    private readonly WriterService _writerService;

    public WritersController(WriterService writerService)
    {
        _writerService = writerService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<WriterResponseTo>>> GetWriters([FromQuery] QueryParams queryParams)
    {
        var writers = await _writerService.GetAllAsync(queryParams);
        return Ok(writers);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<WriterResponseTo>> GetWriter(int id)
    {
        var writer = await _writerService.GetByIdAsync(id);
        if (writer == null)
        {
            return NotFound();
        }
        return Ok(writer);
    }

    [HttpPost]
    public async Task<ActionResult<WriterResponseTo>> CreateWriter(WriterRequestTo writerRequest)
    {
        try
        {
            var writer = await _writerService.CreateAsync(writerRequest);
            return CreatedAtAction(nameof(GetWriter), new { id = writer.Id }, writer);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(/*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(StatusCodes.Status403Forbidden); // 409 Conflict
        }
    }

    [HttpPut]
    public async Task<ActionResult<WriterResponseTo>> UpdateWriter(WriterRequestTo writerRequest)
    {
        try
        {
            var writer = await _writerService.UpdateAsync(writerRequest);
            if (writer == null)
            {
                return NotFound(); // 404 Not Found
            }
            return Ok(writer);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(/*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(StatusCodes.Status403Forbidden); // 409 Conflict
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteWriter(int id)
    {
        var isDeleted = await _writerService.DeleteAsync(id);
        if (!isDeleted)
        {
            return NotFound(); // 404 Not Found
        }

        return NoContent(); // 204 No Content
    }
}