using Lab1.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/stickers")]
    public class StickerController : ControllerBase
    {
        private IStickerService _stickerService;
        private IMapper _mapper;
        public StickerController(IStickerService stickerService, IMapper mapper) => (_stickerService, _mapper) = (stickerService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] StickerRequestTo dto)
        {
            var res = _stickerService.CreateSticker(_mapper.Map<Sticker>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_stickerService.DeleteSticker(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var sticker = _stickerService.GetSticker(id);
            return StatusCode(200, sticker);
        }
        [HttpPut]
        public IActionResult Update([FromBody] StickerUpdateRequest updSticker)
        {
            var sticker = _stickerService.UpdateSticker(_mapper.Map<Sticker>(updSticker), updSticker.Id);
            return StatusCode(200, sticker);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var stickers = _stickerService.GetAllStickers();
            return StatusCode(200, stickers);
        }
    }
}
