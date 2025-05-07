using LAB2.Repositories;

namespace LAB2.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using DTOs;
using Services;

[Route("api/v1.0/[controller]")]
[ApiController]
public class StickersController : ControllerBase
{
    private readonly StickerService _stickerService;

    public StickersController(StickerService stickerService)
    {
        _stickerService = stickerService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<StickerResponseTo>>> GetStickers([FromQuery] QueryParams queryParams)
    {
        var stickers = await _stickerService.GetAllAsync(queryParams);
        return Ok(stickers);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<StickerResponseTo>> GetSticker(int id)
    {
        var sticker = await _stickerService.GetByIdAsync(id);
        if (sticker == null)
        {
            return NotFound();
        }
        return Ok(sticker);
    }

    [HttpPost]
    public async Task<ActionResult<StickerResponseTo>> CreateSticker(StickerRequestTo stickerRequest)
    {
        try
        {
            var sticker = await _stickerService.CreateAsync(stickerRequest);
            return CreatedAtAction(nameof(GetSticker), new { id = sticker.Id }, sticker);
        }
        catch (ArgumentException ex)
        {
            return BadRequest( /*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(403 /*ex.Message*/); // 409 Conflict
        }
        catch (Microsoft.EntityFrameworkCore.DbUpdateException ex)
        {
            return Conflict( /*ex.Message*/);
        }
    }

    [HttpPut]
    public async Task<ActionResult<StickerResponseTo>> UpdateSticker(StickerRequestTo stickerRequest)
    {
        try
        {
            var sticker = await _stickerService.UpdateAsync(stickerRequest);
            if (sticker == null)
            {
                return NotFound();
            }

            return Ok(sticker);
        }
        catch (ArgumentException ex)
        {
            return BadRequest( /*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(403 /*ex.Message*/); // 409 Conflict
        }
        catch (Microsoft.EntityFrameworkCore.DbUpdateException ex)
        {
            return Conflict( /*ex.Message*/);
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteSticker(int id)
    {
        var isDeleted = await _stickerService.DeleteAsync(id);
        if (!isDeleted)
        {
            return NotFound(); // 404 Not Found
        }

        return NoContent(); // 204 No Content
    }
}