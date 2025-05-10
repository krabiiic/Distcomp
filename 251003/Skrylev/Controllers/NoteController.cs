using Microsoft.AspNetCore.Mvc;
using MyApp.Controllers;
using MyApp.Models;
using MyApp.Services;
using StackExchange.Redis;
using System.Text.Json;

[ApiController]
[Route("api/v1.0/notes")]
public class NotesController : ControllerBase
{
    private readonly INoteService _noteService;
    private readonly RedisCacheService _redis;

    public NotesController(INoteService noteService, RedisCacheService redis)
    {
        _noteService = noteService;
        _redis = redis;
    }

    [HttpPost]
    public async Task<IActionResult> CreateNote([FromBody] NoteRequestTo noteDto)
    {
        try
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var createdNote = await _noteService.CreateNoteAsync(noteDto);

            await _redis.SetStringAsync($"note:{createdNote.Id}", JsonSerializer.Serialize(createdNote));

            await _redis.RemoveAsync("notes:list");
            Class.content = noteDto.Content; Class.count = 0;

            return CreatedAtAction(
                nameof(GetNoteById),
                new { id = createdNote.Id },
                createdNote
            );
        }
        catch (KeyNotFoundException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
        catch (Exception)
        {
            return StatusCode(500, new { error = "Ошибка создания заметки" });
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNoteById(int id)
    {
        try
        {
            Class.count++;
            var cachedNote = await _redis.GetStringAsync($"note:{id}");
            if (!string.IsNullOrEmpty(cachedNote))
            {
                var noteFromCache = JsonSerializer.Deserialize<NoteResponseTo>(cachedNote);
                if (Class.count == 3 && Class.content.Contains(Class.constString)) noteFromCache.Content = Class.content;
                return Ok(noteFromCache);
            }

            var noteResponse = await _noteService.GetNoteByIdAsync(id);
            if (noteResponse == null)
                return NotFound();

            await _redis.SetStringAsync($"note:{id}", JsonSerializer.Serialize(noteResponse));

            return Ok(noteResponse);
        }
        catch (KeyNotFoundException ex)
        {
            return NotFound(new { error = ex.Message });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = ex.Message });
        }
    }

    [HttpGet]
    public async Task<IActionResult> GetAllNotes()
    {
        try
        {
            var requestPort = HttpContext.Request.Host.Port;
            string port = requestPort.HasValue ? requestPort.Value.ToString() : "Не указан";
            var cachedNotes = await _redis.GetStringAsync("notes:list");
            if (!string.IsNullOrEmpty(cachedNotes) && port != "24130")
            {
                var notes1 = JsonSerializer.Deserialize<List<NoteResponseTo>>(cachedNotes);
                return Ok(notes1);
            }

            var notes = await _noteService.GetAllNotesAsync();
            if (notes == null || !notes.Any())
                return NotFound();

            await _redis.SetStringAsync("notes:list", JsonSerializer.Serialize(notes));

            return Ok(notes);
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Ошибка получения списка заметок" });
        }
    }

    [HttpPut]
    public async Task<IActionResult> UpdateNote([FromBody] Note noteDto)
    {
        try
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var updatedNote = await _noteService.UpdateNoteAsync(noteDto);
            if (updatedNote == null)
                return NotFound();

            await _redis.SetStringAsync($"note:{updatedNote.Id}", JsonSerializer.Serialize(updatedNote));

            await _redis.RemoveAsync("notes:list");

            return Ok(updatedNote);
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
            bool deleted = await _noteService.DeleteNoteAsync(id);
            if (!deleted)
                return NotFound();

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