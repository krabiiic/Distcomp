using Microsoft.AspNetCore.Mvc;
using discussion.Services.Interfaces;
using discussion.DTO.Response;
using discussion.DTO.Request;

namespace discussion.Controllers
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController(INoteService service) : ControllerBase
    {
        [HttpGet("{id:long}")]
        public async Task<ActionResult<NoteResponseTo>> GetNote(long id)
        {
            return Ok(await service.GetNoteById(id));
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<NoteResponseTo>>> GetNotes()
        {
            return Ok(await service.GetNotes());
        }

        [HttpPost]
        public async Task<ActionResult<NoteResponseTo>> CreateNote(NoteRequestTo noteRequestTo)
        {
            NoteResponseTo addedNote = await service.CreateNote(noteRequestTo);
            return CreatedAtAction(nameof(GetNote), new { id = addedNote.Id }, addedNote);
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> DeleteNote(long id)
        {
            await service.DeleteNote(id);
            return NoContent();
        }

        [HttpPut]
        public async Task<ActionResult<NoteResponseTo>> UpdateNote(NoteRequestTo updateNoteRequestTo)
        {
            return Ok(await service.UpdateNote(updateNoteRequestTo));
        }
        
    }
}
