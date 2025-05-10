using Microsoft.AspNetCore.Mvc;
using lab2_jpa.Models;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Controllers;

[Route("api/v1.0/notes")]
[ApiController]
public class NoteController(INoteService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Note>> GetNote(long id)
    {
        return Ok(await service.GetNoteById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Note>>> GetNotes()
    {
        return Ok(await service.GetNotes());
    }

    [HttpPost]
    public async Task<ActionResult<NoteResponseTo>> CreateNote(CreateNoteRequestTo createNoteRequestTo)
    {
        var addedNote = await service.CreateNote(createNoteRequestTo);
        return CreatedAtAction(nameof(GetNote), new { id = addedNote.id }, addedNote);
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
        return Ok(await service.UpdateNote(updateNoteRequestTo));
    }
}