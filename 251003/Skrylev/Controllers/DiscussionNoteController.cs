using Microsoft.AspNetCore.Mvc;
using MyApp.Models;
using System.Text.Json;

[ApiController]
[Route("localhost:24130/api/v1.0/notes")]
public class DiscussionNotesController : ControllerBase
{
    private readonly IDiscussionNoteService _noteService;
    private readonly RedisCacheService _redis;

    public DiscussionNotesController(
        IDiscussionNoteService noteService,
        RedisCacheService redis)
    {
        _noteService = noteService;
        _redis = redis;
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNoteById(int id)
    {
        try
        {
            var cachedNote = await _redis.GetStringAsync($"note:{id}");
            if (!string.IsNullOrEmpty(cachedNote))
            {
                var noteFromCache = JsonSerializer.Deserialize<Note>(cachedNote);
                return Ok(noteFromCache);
            }

            var note = await _noteService.GetNoteByIdAsync(id);
            if (note == null) return NotFound();

            await _redis.SetStringAsync($"note:{id}", JsonSerializer.Serialize(note));
            return Ok(note);
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Ошибка получения заметки" });
        }
    }

    [HttpPost]
    public async Task<IActionResult> CreateNote([FromBody] Note noteDto)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);

        try
        {
            var createdNote = await _noteService.CreateNoteAsync(noteDto);
            await _redis.SetStringAsync($"note:{createdNote.id}", JsonSerializer.Serialize(createdNote));
            await _redis.RemoveAsync("notes:list");
            return CreatedAtAction(nameof(GetNoteById), new { id = createdNote.id }, createdNote);
        }
        catch (Exception)
        {
            return StatusCode(500, new { error = "Ошибка создания заметки" });
        }
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateNote(int id, [FromBody] Note noteDto)
    {
        if (id != noteDto.id)
            return BadRequest("ID не совпадает");

        try
        {
            var updated = await _noteService.UpdateNoteAsync(noteDto);
            if (!updated) return NotFound();

            await _redis.SetStringAsync($"note:{id}", JsonSerializer.Serialize(noteDto));
            await _redis.RemoveAsync("notes:list");
            return Ok(noteDto);
        }
        catch (Exception)
        {
            return StatusCode(500, new { error = "Ошибка обновления заметки" });
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNote(int id)
    {
        try
        {
            var deleted = await _noteService.DeleteNoteAsync(id);
            if (!deleted) return NotFound();

            await _redis.RemoveAsync($"note:{id}");
            await _redis.RemoveAsync("notes:list");
            return NoContent();
        }
        catch (Exception)
        {
            return StatusCode(500, new { error = "Ошибка удаления заметки" });
        }
    }
}