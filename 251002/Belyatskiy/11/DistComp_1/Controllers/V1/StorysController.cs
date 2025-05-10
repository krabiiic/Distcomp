using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class ArticlesController : ControllerBase
{
    private readonly IArticleService _ArticleService;

    public ArticlesController(IArticleService ArticleService)
    {
        _ArticleService = ArticleService;
    }

    [HttpGet]
    public async Task<IActionResult> GetStories()
    {
        var stories = await _ArticleService.GetStoriesAsync();
        return Ok(stories);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetArticleById(long id)
    {
        var Article = await _ArticleService.GetArticleByIdAsync(id);
        return Ok(Article);
    }

    [HttpPost]
    public async Task<IActionResult> CreateArticle([FromBody] ArticleRequestDTO Article)
    {
        var createdArticle = await _ArticleService.CreateArticleAsync(Article);
        return CreatedAtAction(nameof(CreateArticle), new { id = createdArticle.Id }, createdArticle);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateArticle([FromBody] ArticleRequestDTO Article)
    {
        var updatedArticle = await _ArticleService.UpdateArticleAsync(Article);
        return Ok(updatedArticle);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteArticle(long id)
    {
        await _ArticleService.DeleteArticleAsync(id);
        return NoContent();
    }
}