using Core.DTO;
using Core.Interfaces;
using Core.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/articles")]
public class ArticleController(IArticleService _articleService, ICreatorService _creatorService,
    ITagService _tagService, IRedisCacheService _redis) : ControllerBase
{
    private readonly string _cachePrefix = "article";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);

    [HttpGet]
    public async Task<IActionResult> GetAllArticles()
    {
        var article = await _articleService.GetArticle(new ArticleRequestToGetAll());
        return Ok(article);
    }


    [HttpGet("{id:long}/creator")]
    public async Task<IActionResult> GetCreatorByArticleId(long id)
    {
        CreatorResponseToGetById creator =
            await _creatorService.GetCreatorByArticleId(new CreatorRequestToGetByArticleId() { TagId = id });

        return Ok(creator);
    }

    [HttpGet("{id:long}/messages")]
    public async Task<IActionResult> GetMessagesByArticleId(long id)
    {
        throw new NotImplementedException();
    }
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetArticleById(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<ArticleResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }

        ArticleResponseToGetById article = await _articleService.GetArticleById(new ArticleRequestToGetById() { Id = id });

        await _redis.SetCacheValueAsync<ArticleResponseToGetById>(
            $"{_cachePrefix}:{id}",
            article,
            _cacheExpiration
        );
        return Ok(article);
    }

    [HttpPost]
    public async Task<IActionResult> CreateArticle(ArticleRequestToCreate articleRequestToCreate)
    {
        IEnumerable<long>? tagIds = null;
        if (articleRequestToCreate.Tags != null)
        {
            tagIds = (await _tagService.CreateTagsIfDontExist(articleRequestToCreate.Tags)).Select(tag => tag.Id);
        }
        var article = await _articleService.CreateArticle(articleRequestToCreate, tagIds);
        return CreatedAtAction(nameof(GetArticleById), new { id = article.Id }, article);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateArticle(ArticleRequestToFullUpdate articleModel)
    {
        var article = await _articleService.UpdateArticle(articleModel);

        var redisKey = $"{_cachePrefix}:{article.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);

        return Ok(article);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteArticle(long id)
    {
        var article = await _articleService.DeleteArticle(new ArticleRequestToDeleteById() { Id = id });

        var redisKey = $"{_cachePrefix}:{article.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return NoContent();
    }
}