using Microsoft.AspNetCore.Mvc;
using publisher.DTO.Request;
using publisher.DTO.Response;
using publisher.Models;
using publisher.Services.Interfaces;

namespace publisher.Controllers
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
        public async Task<ActionResult<NoteResponseTo>> CreateNote(CreateNoteRequestTo createNoteRequestTo)
        {
            var country = GetCountryFromRequest();
            NoteResponseTo addedNote = await service.CreateNote(createNoteRequestTo, country);
            return CreatedAtAction(nameof(GetNote), new { id = addedNote.Id }, addedNote);
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> DeleteNote(long id)
        {
            await service.DeleteNote(id);
            return NoContent();
        }

        [HttpPut]
        public async Task<ActionResult<NoteResponseTo>> UpdateNote(UpdateNoteRequestTo updateNoteRequestTo)
        {
            var country = GetCountryFromRequest();
            return Ok(await service.UpdateNote(updateNoteRequestTo, country));
        }

        private string GetCountryFromRequest() =>
            Request.GetTypedHeaders().AcceptLanguage
                .MaxBy(x => x.Quality ?? 1)
                ?.Value.ToString() ?? System.Globalization.CultureInfo.CurrentCulture.Name;
    }
}
