using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients;
using Publisher.HttpClients.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class NotesController : ControllerBase
{
    private readonly IDiscussionClient _noteClient;

    public NotesController(IDiscussionClient noteClient)
    {
        _noteClient = noteClient;
    }

    [HttpGet]
    public async Task<IActionResult> GetNotes()
    {
        var notes = await _noteClient.GetNotesAsync();
        return Ok(notes);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNoteById(long id)
    {
        var note = await _noteClient.GetNoteByIdAsync(id);
        return Ok(note);
    }

    [HttpPost]
    public async Task<IActionResult> CreateNote([FromBody] NoteRequestDTO note)
    {
        var createdNote = await _noteClient.CreateNoteAsync(note);
        return CreatedAtAction(nameof(CreateNote), new { id = createdNote.Id }, createdNote);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateNote(long id, [FromBody] NoteRequestDTO note)
    {
        var updatedNote = await _noteClient.UpdateNoteAsync(id, note);
        return Ok(updatedNote);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNote(long id)
    {
        await _noteClient.DeleteNoteAsync(id);
        return NoContent();
    }
}