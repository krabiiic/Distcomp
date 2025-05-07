using Application.abstractions;
using Application.services;
using Core;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;

namespace rv_lab_1.controllers
{
    [Route("api/v1.0/stories")]
    [ApiController]
    public class StoryController(IStoryService storyService) : ControllerBase
    {
        private readonly IStoryService _storyService = storyService;
        [HttpGet("{id:long}")]
        public async Task<ActionResult<StoryResponseTo?>> GetByIdAsync(long id)
        {
            var res = await _storyService.GetByIdAsync(id);
            if (res == null)
                return StatusCode(211);
            return res;
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
                return StatusCode(403);
            return Created(string.Empty, res);
        }

        [HttpPut]
        public async Task<ActionResult<StoryResponseTo>> Put([FromBody] Story requestTo)
        {
            var res = await _storyService.UpdateAsync(requestTo.Id, new StoryRequestTo() 
                { Content = requestTo.Content, EditorId = requestTo.EditorId, Title = requestTo.Title});
            return Ok(res);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<StoryResponseTo>> PutWitnId(int id, [FromBody] StoryRequestTo requestTo)
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
