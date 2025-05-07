using DistComp.DTO.RequestDTO;
using DistComp.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class AuthorsController : ControllerBase
{
    private readonly IAuthorService _authorService;

    public AuthorsController(IAuthorService authorService)
    {
        _authorService = authorService;
    }

    [HttpGet]
    public async Task<IActionResult> GetAuthors()
    {
        var authors = await _authorService.GetAuthorsAsync();
        return Ok(authors);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetAuthorById(long id)
    {
        var author = await _authorService.GetAuthorByIdAsync(id);
        return Ok(author);
    }

    [HttpPost]
    public async Task<IActionResult> CreateAuthor([FromBody] AuthorRequestDTO author)
    {
        var createdAuthor = await _authorService.CreateAuthorAsync(author);
        return CreatedAtAction(nameof(CreateAuthor), new { id = createdAuthor.Id }, createdAuthor);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateAuthor([FromBody] AuthorRequestDTO author)
    {
        var updatedAuthor = await _authorService.UpdateAuthorAsync(author);
        return Ok(updatedAuthor);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteAuthor(long id)
    {
        await _authorService.DeleteAuthorAsync(id);
        return NoContent();
    }
}