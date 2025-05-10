using Core.DTO;
using Core.Interfaces;
using Core.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/creators")]
public class CreatorController : ControllerBase
{
    private readonly ICreatorService _creatorService;
    private readonly IRedisCacheService _redis;
    private readonly string _cachePrefix = "creator";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    public CreatorController(ICreatorService service, IRedisCacheService redis)
    {
        _creatorService = service;
        _redis = redis;
    }

    [HttpGet]
    public async Task<IActionResult> GetAllCreators()
    {
        var creators = await _creatorService.GetCreators(new CreatorRequestToGetAll());
        return Ok(creators);
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetCreatorById(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<CreatorResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }

        CreatorResponseToGetById creator = await _creatorService.GetCreatorById(new CreatorRequestToGetById() {Id = id});
        
        await _redis.SetCacheValueAsync<CreatorResponseToGetById>(
            $"{_cachePrefix}:{id}",
            creator,
            _cacheExpiration
        );
        return Ok(creator);
    }
    [HttpPost]
    public async Task<IActionResult> CreateCreator(CreatorRequestToCreate creatorRequestToCreate)
    {
        var creator = await _creatorService.CreateCreator(creatorRequestToCreate);
        return CreatedAtAction(nameof(GetCreatorById), new { id = creator.Id }, creator);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateCreator(CreatorRequestToFullUpdate creatorModel)
    {
        var creator = await _creatorService.UpdateCreator(creatorModel);
        
        var redisKey = $"{_cachePrefix}:{creator.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return Ok(creator);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteCreator(long id)
    {
        var creator = await _creatorService.DeleteCreator(new CreatorRequestToDeleteById(){Id = id});
        
        var redisKey = $"{_cachePrefix}:{creator.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return NoContent();
    }
}