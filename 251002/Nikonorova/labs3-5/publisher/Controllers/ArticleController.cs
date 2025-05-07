using Microsoft.AspNetCore.Mvc;
using publisher.DTO.Request;
using publisher.DTO.Response;
using publisher.Models;
using publisher.Services.Interfaces;

namespace publisher.Controllers
{
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
            return CreatedAtAction(nameof(GetArticle), new { id = addedArticle.Id }, addedArticle);
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
}
