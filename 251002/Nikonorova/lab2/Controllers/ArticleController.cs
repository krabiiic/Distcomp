using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using lab2_jpa.Models;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Controllers;

[Route("api/v1.0/articles")]
[ApiController]
public class ArticleController(IArticleService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Article>> GetArticle(long id)
    {
        return Ok(await service.GetArticleById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Article>>> GetArticles()
    {
        return Ok(await service.GetArticles());
    }

    [HttpPost]
    public async Task<ActionResult<ArticleResponseTo>> CreateArticle(CreateArticleRequestTo createArticleRequestTo)
    {
        var addedArticle = await service.CreateArticle(createArticleRequestTo);
        return CreatedAtAction(nameof(GetArticle), new { id = addedArticle.id }, addedArticle);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteArticle(long id)
    {
        await service.DeleteArticle(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<ArticleResponseTo>> UpdateArticle(UpdateArticleRequestTo updateArticleRequestTo)
    {
        return Ok(await service.UpdateArticle(updateArticleRequestTo));
    }
}