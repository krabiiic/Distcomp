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

[Route("api/v{version:apiVersion}/tags")]
[ApiVersion("1.0")]
[ApiController]
public class TagController(ITagService service, IOutputCacheStore cacheStore) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Tag>> GetTag(long id)
    {
        return Ok(await service.GetTagById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Tag>>> GetTags()
    {
        return Ok(await service.GetTags());
    }

    [HttpPost]
    public async Task<ActionResult<TagResponseTo>> CreateTag(CreateTagRequestTo createTagRequestTo)
    {
        TagResponseTo addedTag = await service.CreateTag(createTagRequestTo);
        await cacheStore.EvictByTagAsync(CacheTags.Tags, HttpContext.RequestAborted);
        return CreatedAtAction(nameof(GetTag), new { id = addedTag.Id }, addedTag);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteTag(long id)
    {
        await service.DeleteTag(id);
        await cacheStore.EvictByTagAsync(CacheTags.Tags, HttpContext.RequestAborted);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<TagResponseTo>> UpdateTag(UpdateTagRequestTo updateTagRequestTo)
    {
        TagResponseTo updateTag = await service.UpdateTag(updateTagRequestTo);
        await cacheStore.EvictByTagAsync(CacheTags.Tags, HttpContext.RequestAborted);
        return Ok(updateTag);
    }
}