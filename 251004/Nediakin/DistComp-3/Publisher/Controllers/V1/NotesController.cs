using Publisher.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.HttpClients.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class NotesController : ControllerBase
{
    private readonly IDiscussionClient _discussionClient;

    public NotesController(IDiscussionClient discussionClient)
    {
        _discussionClient = discussionClient;
    }

    [HttpGet]
    public async Task<IActionResult> GetNotes()
    {
        var notes = await _discussionClient.GetNotesAsync();
        return Ok(notes);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNoteById(long id)
    {
        var note = await _discussionClient.GetNoteByIdAsync(id);
        return Ok(note);
    }

    [HttpPost]
    public async Task<IActionResult> CreateNote([FromBody] NoteRequestDTO note)
    {
        var createdNote = await _discussionClient.CreateNoteAsync(note);
        return CreatedAtAction(nameof(CreateNote), new { id = createdNote.Id }, createdNote);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateNote([FromBody] NoteRequestDTO note)
    {
        var updatedNote = await _discussionClient.UpdateNoteAsync(note);
        return Ok(updatedNote);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNote(long id)
    {
        await _discussionClient.DeleteNoteAsync(id);
        return NoContent();
    }
}