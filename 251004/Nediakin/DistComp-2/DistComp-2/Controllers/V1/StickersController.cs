using DistComp.DTO.RequestDTO;
using DistComp.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class StickersController : ControllerBase
{
    private readonly IStickerService _stickerService;

    public StickersController(IStickerService stickerService)
    {
        _stickerService = stickerService;
    }

    [HttpGet]
    public async Task<IActionResult> GetStickers()
    {
        var stickers = await _stickerService.GetStickersAsync();
        return Ok(stickers);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetStickerById(long id)
    {
        var sticker = await _stickerService.GetStickerByIdAsync(id);
        return Ok(sticker);
    }

    [HttpPost]
    public async Task<IActionResult> CreateSticker([FromBody] StickerRequestDTO sticker)
    {
        var createdSticker = await _stickerService.CreateStickerAsync(sticker);
        return CreatedAtAction(nameof(CreateSticker), new { id = createdSticker.Id }, createdSticker);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateSticker([FromBody] StickerRequestDTO sticker)
    {
        var updatedSticker = await _stickerService.UpdateStickerAsync(sticker);
        return Ok(updatedSticker);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteSticker(long id)
    {
        await _stickerService.DeleteStickerAsync(id);
        return NoContent();
    }
}