using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class NewsController : ControllerBase
{
    private readonly INewservice _newservice;

    public NewsController(INewservice newservice)
    {
        _newservice = newservice;
    }

    [HttpGet]
    public async Task<IActionResult> GetStories()
    {
        var stories = await _newservice.GetStoriesAsync();
        return Ok(stories);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNewsById(long id)
    {
        var news = await _newservice.GetNewsByIdAsync(id);
        return Ok(news);
    }

    [HttpPost]
    public async Task<IActionResult> CreateNews([FromBody] NewsRequestDTO news)
    {
        var createdNews = await _newservice.CreateNewsAsync(news);
        return CreatedAtAction(nameof(CreateNews), new { id = createdNews.Id }, createdNews);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateNews([FromBody] NewsRequestDTO news)
    {
        var updatedNews = await _newservice.UpdateNewsAsync(news);
        return Ok(updatedNews);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNews(long id)
    {
        await _newservice.DeleteNewsAsync(id);
        return NoContent();
    }
}