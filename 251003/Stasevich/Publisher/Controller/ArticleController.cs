using Microsoft.AspNetCore.Mvc;
using WebApplication1.DTO;
using WebApplication1.Repository;
using WebApplication1.Service;

namespace WebApplication1.Controller
{
    [ApiController]
    [Route("api/v1.0/articles")]
    public class ArticleController : ControllerBase
    {
        private readonly IArticleService _articleService;
        public ArticleController(IArticleService articleService) => _articleService = articleService;

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ArticleResponseTo>>> GetAll(
            [FromQuery] int pageNumber = 1,
            [FromQuery] int pageSize = 10,
            [FromQuery] string? sortBy = null,
            [FromQuery] string? filter = null)
        {
            try
            {
                var result = await _articleService.GetAllArticlesAsync(pageNumber, pageSize, sortBy, filter);
                return Ok(result.Items);
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
            catch (Exception)
            {
                return StatusCode(500, new { error = "Internal server error", code = "50000" });
            }
        }


        [HttpGet("{id:long}")]
        public async Task<ActionResult<ArticleResponseTo>> GetById(long id)
        {
            try
            {
                var article = await _articleService.GetArticleByIdAsync(id);
                return Ok(article);
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
        }

        [HttpPost]
        public async Task<ActionResult<ArticleResponseTo>> Create([FromBody] ArticleRequestTo dto)
        {
            try
            {
                var created = await _articleService.CreateArticleAsync(dto);
                return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
            catch (Exception)
            {
                return StatusCode(500, new { error = "Internal server error", code = "50000" });
            }
        }

        [HttpPut]
        public async Task<ActionResult<ArticleResponseTo>> Update([FromBody] ArticleRequestTo dto)
        {
            if (dto.Id == null || dto.Id <= 0)
            {
                return BadRequest(new { error = "Invalid ID", code = "40001" });
            }

            try
            {
                var updated = await _articleService.UpdateArticleAsync(dto.Id.Value, dto);
                return Ok(updated);
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> Delete(long id)
        {
            try
            {
                await _articleService.DeleteArticleAsync(id);
                return NoContent();
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
        }
    }
}
