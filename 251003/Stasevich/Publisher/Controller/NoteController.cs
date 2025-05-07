using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using WebApplication1.DTO;
using WebApplication1.Service;

namespace WebApplication1.Controller
{
    [ApiController]
    [Route("api/v1.0/notes")]
    public class NoteController : ControllerBase
    {
        private readonly IRemoteNoteService _remoteNoteService;

        public NoteController(IRemoteNoteService remoteNoteService)
        {
            _remoteNoteService = remoteNoteService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<NoteResponseTo>>> GetAll()
        {
            var result = await _remoteNoteService.GetAllNotesAsync();
            return Ok(result);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<NoteResponseTo>> GetById(string id)
        {
            if (!long.TryParse(id, out var noteId))
                return BadRequest("Неверный формат идентификатора.");
            var note = await _remoteNoteService.GetNoteByIdAsync(noteId);
            if (note == null)
                return NotFound();
            return Ok(note);
        }

        [HttpPost]
        public async Task<ActionResult<NoteResponseTo>> Create([FromBody] NoteRequestTo dto)
        {
            var created = await _remoteNoteService.CreateNoteAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
        }

        [HttpPut]
        public async Task<ActionResult<NoteResponseTo>> Update([FromBody] NoteRequestTo dto)
        {
            if (!dto.Id.HasValue)
                return BadRequest("ID in request body is required.");
            var updated = await _remoteNoteService.UpdateNoteAsync(dto);
            return Ok(updated);
        }

        [HttpPut("{id:long}")]
        public async Task<ActionResult<NoteResponseTo>> Update(
            [FromRoute] long id,
            [FromBody] NoteRequestTo dto)
        {
            if (dto?.Id != id) 
            {
                dto.Id = id;
            }

            var updated = await _remoteNoteService.UpdateNoteAsync(dto);
            return Ok(updated);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            if (!long.TryParse(id, out var noteId))
                return BadRequest("Неверный формат идентификатора.");
            await _remoteNoteService.DeleteNoteAsync(id);
            return NoContent();
        }
    }
}
