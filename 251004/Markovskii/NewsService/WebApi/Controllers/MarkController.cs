using Application.DTO.Request.Mark;
using Application.DTO.Response.Mark;
using Application.Interfaces;
using Infrastructure.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/marks")]
public class MarkController  (IMarkService _markService, IRedisCacheService _redis) : ControllerBase
{
    private readonly string _cachePrefix = "mark";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    
    [HttpGet]
    public async Task<IActionResult> GetAllMark()
    {
        var mark = await _markService.GetMarks(new MarkRequestToGetAll());
        return Ok(mark);
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetMarkById(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<MarkResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }
        
        MarkResponseToGetById mark = await _markService.GetMarkById(new MarkRequestToGetById() {Id = id});
        
        await _redis.SetCacheValueAsync<MarkResponseToGetById>(
            $"{_cachePrefix}:{id}",
            mark,
            _cacheExpiration
        );
        return Ok(mark);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMark(MarkRequestToCreate markRequestToCreate)
    {
        var mark = await _markService.CreateMark(markRequestToCreate);
        return CreatedAtAction(nameof(GetMarkById), new { id = mark.Id }, mark);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateMark(MarkRequestToFullUpdate markModel)
    {
        var mark = await _markService.UpdateMark(markModel);
        
        var redisKey = $"{_cachePrefix}:{mark.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return Ok(mark);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteMark(long id)
    {
        var mark = await _markService.DeleteMark(new MarkRequestToDeleteById(){Id = id});
        
        var redisKey = $"{_cachePrefix}:{mark.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        return NoContent();
    }
}