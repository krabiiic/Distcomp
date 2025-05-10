using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class NotesController : ControllerBase
{
    private readonly INoteService _noteService;

    public NotesController(INoteService noteService)
    {
        _noteService = noteService;
    }

    [HttpGet]
    public async Task<IActionResult> GetNotes()
    {
        var notes = await _noteService.GetNotesAsync();
        return Ok(notes);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNoteById(long id)
    {
        var note = await _noteService.GetNoteByIdAsync(id);
        return Ok(note);
    }

    [HttpPost]
    public async Task<IActionResult> CreateNote([FromBody] NoteRequestDTO note)
    {
        var createdNote = await _noteService.CreateNoteAsync(note);
        return CreatedAtAction(nameof(CreateNote), new { id = createdNote.Id }, createdNote);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateNote([FromBody] NoteRequestDTO note)
    {
        var updatedNote = await _noteService.UpdateNoteAsync(note);
        return Ok(updatedNote);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNote(long id)
    {
        await _noteService.DeleteNoteAsync(id);
        return NoContent();
    }
}