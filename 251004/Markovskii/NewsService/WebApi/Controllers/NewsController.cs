using Application.DTO.Request;
using Application.DTO.Request.Editor;
using Application.DTO.Request.News;
using Application.DTO.Request.Post;
using Application.DTO.Response;
using Application.DTO.Response.Editor;
using Application.DTO.Response.Mark;
using Application.DTO.Response.News;
using Application.Interfaces;
using Infrastructure.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/news")]
public class NewsController (INewsService _newsService, IEditorService _editorService,
    IMarkService _markService, IRedisCacheService _redis) : ControllerBase
{
    private readonly string _cachePrefix = "news";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    
    [HttpGet]
    public async Task<IActionResult> GetAllNews()
    {
        var news = await _newsService.GetNews(new NewsRequestToGetAll());
        return Ok(news);
    }
        
        
        [HttpGet("{id:long}/editor")]
        public async Task<IActionResult> GetEditorByNewsId(long id)
        {
            EditorResponseToGetById editor =
                await _editorService.GetEditorByNewsId(new EditorRequestToGetByNewsId() { NewsId = id });
            
            return Ok(editor);
        }
        
        [HttpGet("{id:long}/posts")]
        public async Task<IActionResult> GetPostsByNewsId(long id)
        {
            throw new NotImplementedException();
            //var posts = await _postService.GetPostsByNewsId(new PostRequestToGetByNewsId() { NewsId = id });
            //return Ok(posts);
        }
        [HttpGet("{id:long}")]
        public async Task<IActionResult> GetNewsById(long id)
        {
            var cachedResult = await _redis.GetCacheValueAsync<NewsResponseToGetById?>(
                $"{_cachePrefix}:{id}"
            );

            if (cachedResult is not null)
            {
                return StatusCode(StatusCodes.Status200OK, cachedResult);
            }
            
            NewsResponseToGetById news = await _newsService.GetNewsById(new NewsRequestToGetById() {Id = id});
            
            await _redis.SetCacheValueAsync<NewsResponseToGetById>(
                $"{_cachePrefix}:{id}",
                news,
                _cacheExpiration
            );
            return Ok(news);
        }

        [HttpPost]
        public async Task<IActionResult> CreateNews(NewsRequestToCreate newsRequestToCreate)
        {
            IEnumerable<long>? markIds = null;
            if (newsRequestToCreate.Marks != null)
            {
                    markIds = (await _markService.CreateMarksIfDontExist(newsRequestToCreate.Marks)).Select(mark => mark.Id);
            }
            var news = await _newsService.CreateNews(newsRequestToCreate,markIds);
            return CreatedAtAction(nameof(GetNewsById), new { id = news.Id }, news);
        }

        [HttpPut]
    public async Task<IActionResult> UpdateNews(NewsRequestToFullUpdate newsModel)
    {
        var news = await _newsService.UpdateNews(newsModel);
        
        var redisKey = $"{_cachePrefix}:{news.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return Ok(news);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteNews(long id)
    {
        var news = await _newsService.DeleteNews(new NewsRequestToDeleteById(){Id = id});
        
        var redisKey = $"{_cachePrefix}:{news.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return NoContent();
    }
}