using Core.DTO;
using Core.Interfaces;
using Core.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/tags")]
public class TagController  (ITagService _tagService, IRedisCacheService _redis) : ControllerBase
{
    private readonly string _cachePrefix = "tag";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    
    [HttpGet]
    public async Task<IActionResult> GetAllTag()
    {
        var tag = await _tagService.GetTags(new TagRequestToGetAll());
        return Ok(tag);
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetTagById(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<TagResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }
        
        TagResponseToGetById tag = await _tagService.GetTagById(new TagRequestToGetById() {Id = id});
        
        await _redis.SetCacheValueAsync<TagResponseToGetById>(
            $"{_cachePrefix}:{id}",
            tag,
            _cacheExpiration
        );
        return Ok(tag);
    }

    [HttpPost]
    public async Task<IActionResult> CreateTag(TagRequestToCreate tagRequestToCreate)
    {
        var tag = await _tagService.CreateTag(tagRequestToCreate);
        return CreatedAtAction(nameof(GetTagById), new { id = tag.Id }, tag);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateTag(TagRequestToFullUpdate tagModel)
    {
        var tag = await _tagService.UpdateTag(tagModel);
        
        var redisKey = $"{_cachePrefix}:{tag.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return Ok(tag);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteTag(long id)
    {
        var tag = await _tagService.DeleteTag(new TagRequestToDeleteById(){Id = id});
        
        var redisKey = $"{_cachePrefix}:{tag.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return NoContent();
    }
}