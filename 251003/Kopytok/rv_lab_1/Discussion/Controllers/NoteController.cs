using Core;
using Discussion.abstractions;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace rv_lab_1.controllers
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController(INoteService noteService) : ControllerBase
    {
        private readonly INoteService _noteService = noteService;

        [HttpGet("{id:long}")]
        public async Task<ActionResult<NoteResponseTo?>> GetByIdAsync(long id)
        {
            var res = await _noteService.GetByIdAsync(id);
            if (res == null)
                return StatusCode(400);
            return Ok(res);
        }

        [HttpGet]
        public async Task<IEnumerable<NoteResponseTo>> GetAllAsync()
        {
            return await _noteService.GetAllAsync();
        }

        [HttpPost]
        public async Task<ActionResult<NoteResponseTo>> PostAsync([FromBody] NoteRequestTo requestTo)
        {
            var res = await _noteService.AddNoteRespAsync(requestTo);
            if (res == null)
                return StatusCode(403);
            return Created(string.Empty, res);
        }

        [HttpPut]
        public async Task<ActionResult<NoteResponseTo>> Put([FromBody] Note note)
        {
            Console.WriteLine($"Received: id={note.Id}, storyId={note.StoryId}, content={note.Content}");
            var res = await _noteService.UpdateAsync(note.Id, new NoteRequestTo 
                { Content = note.Content, StoryId = note.StoryId});
            var response = new NoteResponseTo { Id =  note.Id, StoryId = note.StoryId, Content = note.Content };
            return Ok(response);
        }

        //[HttpPut("{id}")]
        //public async Task<ActionResult> PutId(long id, [FromBody] NoteRequestTo requestTo)
        //{
        //    var res = await _noteService.UpdateAsync(id, requestTo);
        //    return Ok();
        //}

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(long id)
        {
            var res = await _noteService.DeleteNoteAsync(id);
            if (!res)
                return BadRequest();
            return NoContent();
        }
    }
}
