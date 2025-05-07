using Application.abstractions;
using Application.services;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;

namespace rv_lab_1.controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class StoryController(IStoryService storyService) : ControllerBase
    {
        private readonly IStoryService _storyService = storyService;
        [HttpGet("{id:long}")]
        public async Task<StoryResponseTo> GetByIdAsync(long id)
        {
            return await _storyService.GetByIdAsync(id);
        }

        [HttpGet]
        public async Task<IEnumerable<StoryResponseTo>> GetAllAsync()
        {
            return await _storyService.GetAllAsync();
        }

        [HttpPost]
        public async Task<ActionResult<StoryResponseTo>> PostAsync([FromBody] StoryRequestTo requestTo)
        {
            var res = await _storyService.CreateAsync(requestTo);
            if (res == null)
                return BadRequest();
            return Created(string.Empty, res);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<StoryRequestTo>> Put(long id, [FromBody] StoryRequestTo requestTo)
        {
            var res = await _storyService.UpdateAsync(id, requestTo);
            return Ok(res);
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var res = await _storyService.DeleteAsync(id);
            if (!res)
                return BadRequest();
            return NoContent();
        }
    }
}
