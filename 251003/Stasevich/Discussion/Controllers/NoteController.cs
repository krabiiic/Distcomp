using Discussion.Models;
using Discussion.Services;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;
using Discussion.DTO;

namespace Discussion.Controllers
{
    [ApiController]
    [Route("api/v1.0/notes")]
    public class NoteController : ControllerBase
    {
        private readonly INoteService _noteService;
        public NoteController(INoteService noteService)
        {
            _noteService = noteService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var notes = await _noteService.GetAllNotesAsync();
            return Ok(notes);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(long id)
        {
            var note = await _noteService.GetNoteByIdAsync(id);
            if (note == null)
                return NotFound();
            return Ok(note);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] NoteRequestTo request)
        {
            var note = new Note
            {
                Country = request.Country,
                ArticleId = request.ArticleId,
                Content = request.Content,
            };
            await _noteService.CreateNoteAsync(note);
            return CreatedAtAction(nameof(GetById), new { id = note.Id }, note);
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] NoteRequestTo request)
        {
            if (!request.Id.HasValue)
                return BadRequest("ID в теле запроса обязателен.");

            long id = request.Id.Value;
            var note = await _noteService.GetNoteByIdAsync(id);
            if (note == null)
                return NotFound();

            note.Content = request.Content;
            note.Modified = DateTime.UtcNow;

            await _noteService.UpdateNoteAsync(id, note);
            return Ok(note);
        }

        [HttpPut("{id:long}")]
        public async Task<IActionResult> Update([FromRoute] long id, [FromBody] NoteRequestTo request)
        {
            if (!request.Id.HasValue)
                return BadRequest("ID в теле запроса обязателен.");

            if (request.Id.Value != id)
            {
                request.Id = id;
            }

            var note = await _noteService.GetNoteByIdAsync(id);
            if (note == null)
                return NotFound();

            note.Content = request.Content;
            note.Modified = DateTime.UtcNow;

            await _noteService.UpdateNoteAsync(id, note);
            return Ok(note);
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(long id)
        {
            await _noteService.DeleteNoteAsync(id);
            return NoContent();
        }
    }
}
