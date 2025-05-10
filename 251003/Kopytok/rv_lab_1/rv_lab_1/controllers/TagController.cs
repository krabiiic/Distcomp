using Application.abstractions;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;

namespace rv_lab_1.controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TagController(ITagService tagService) : ControllerBase
    {
        private readonly ITagService _tagService = tagService;
        [HttpGet("{id:long}")]
        public async Task<TagResponseTo> GetByIdAsync(long id)
        {
            return await _tagService.GetByIdAsync(id);
        }

        [HttpGet]
        public async Task<IEnumerable<TagResponseTo>> GetAllAsync()
        {
            return await _tagService.GetAllAsync();
        }

        [HttpPost]
        public async Task<ActionResult<TagResponseTo>> PostAsync([FromBody] TagRequestTo requestTo)
        {
            var res = await _tagService.CreateAsync(requestTo);
            if (res == null)
                return BadRequest();
            return Created(string.Empty, res);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<TagRequestTo>> Put(long id, [FromBody] TagRequestTo requestTo)
        {
            var res = await _tagService.UpdateAsync(id, requestTo);
            return Ok(res);
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var res = await _tagService.DeleteAsync(id);
            if (!res)
                return BadRequest();
            return NoContent();
        }
    }
}
