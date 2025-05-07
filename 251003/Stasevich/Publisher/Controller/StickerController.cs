using Microsoft.AspNetCore.Mvc;
using WebApplication1.DTO;
using WebApplication1.Repository;
using WebApplication1.Service;

namespace WebApplication1.Controller
{
    [ApiController]
    [Route("api/v1.0/stickers")]
    public class StickerController : ControllerBase
    {
        private readonly IStickerService _stickerService;
        public StickerController(IStickerService stickerService) => _stickerService = stickerService;

        [HttpGet]
        public async Task<ActionResult<IEnumerable<StickerResponseTo>>> GetAll(
            [FromQuery] int pageNumber = 1,
            [FromQuery] int pageSize = 10)
        {
            var result = await _stickerService.GetAllStickersAsync(pageNumber, pageSize);
            return Ok(result.Items);
        }

        [HttpGet("{id:long}")]
        public async Task<ActionResult<StickerResponseTo>> GetById(long id)
        {
            var sticker = await _stickerService.GetStickerByIdAsync(id);
            return Ok(sticker);
        }

        [HttpPost]
        public async Task<ActionResult<StickerResponseTo>> Create([FromBody] StickerRequestTo dto)
        {
            var created = await _stickerService.CreateStickerAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
        }

        [HttpPut]
        public async Task<ActionResult<StickerResponseTo>> Update([FromBody] StickerRequestTo dto)
        {
            if (dto.Id == null)
                return BadRequest("Id must be provided in the request body");
            var updated = await _stickerService.UpdateStickerAsync(dto.Id.Value, dto);
            return Ok(updated);
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> Delete(long id)
        {
            try
            {
                await _stickerService.DeleteStickerAsync(id);
                return NoContent();
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
        }
    }

}
