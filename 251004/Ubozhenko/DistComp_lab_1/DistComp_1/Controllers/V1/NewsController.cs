using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class NewsController : ControllerBase
{
    private readonly INewsService _newsService;

    public NewsController(INewsService newsService)
    {
        _newsService = newsService;
    }

    [HttpGet]
    public async Task<IActionResult> GetNews()
    {
        var news = await _newsService.GetNewsAsync();
        return Ok(news);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetNewsById(long id)
    {
        var news = await _newsService.GetNewsByIdAsync(id);
        return Ok(news);
    }

    [HttpPost]
    public async Task<IActionResult> CreateNews([FromBody] NewsRequestDTO news)
    {
        var createdNews = await _newsService.CreateNewsAsync(news);
        return CreatedAtAction(nameof(CreateNews), new { id = createdNews.Id }, createdNews);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateNews([FromBody] NewsRequestDTO news)
    {
        var updatedNews = await _newsService.UpdateNewsAsync(news);
        return Ok(updatedNews);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteNews(long id)
    {
        await _newsService.DeleteNewsAsync(id);
        return NoContent();
    }
}