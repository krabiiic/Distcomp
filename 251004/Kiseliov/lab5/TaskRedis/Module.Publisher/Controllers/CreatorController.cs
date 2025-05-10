using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OutputCaching;
using Publisher.Constants;
using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Publisher.Models;
using Publisher.Services.Interfaces;
namespace Publisher.Controllers;

[Route("api/v{version:apiVersion}/creators")]
[ApiVersion("1.0")]
[ApiController]
public class CreatorController(ICreatorService service, IOutputCacheStore cacheStore) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Creator>> GetCreator(long id)
    {
        return Ok(await service.GetCreatorById(id));
    }
    
    [HttpGet]
    public async Task<ActionResult<IEnumerable<Creator>>> GetCreators()
    {
        return Ok(await service.GetCreators());
    }

    [HttpPost]
    public async Task<ActionResult<CreatorResponseTo>> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo)
    {
        CreatorResponseTo addedCreator = await service.CreateCreator(createCreatorRequestTo);
        await cacheStore.EvictByTagAsync(CacheTags.Creators, HttpContext.RequestAborted);
        return CreatedAtAction(nameof(GetCreator), new { id = addedCreator.Id }, addedCreator);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteCreator(long id)
    {
        await service.DeleteCreator(id);
        await cacheStore.EvictByTagAsync(CacheTags.Creators, HttpContext.RequestAborted);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<CreatorResponseTo>> UpdateCreator(UpdateCreatorRequestTo updateCreatorRequestTo)
    {
        CreatorResponseTo updatedCreator = await service.UpdateCreator(updateCreatorRequestTo);
        await cacheStore.EvictByTagAsync(CacheTags.Creators, HttpContext.RequestAborted);
        return Ok(updatedCreator);
    }
}