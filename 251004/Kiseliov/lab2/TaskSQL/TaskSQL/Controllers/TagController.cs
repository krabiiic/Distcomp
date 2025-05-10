using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;
using TaskSQL.Services.Interfaces;

namespace TaskSQL.Controllers;

[Route("api/v{version:apiVersion}/tags")]
[ApiVersion("1.0")]
[ApiController]
public class TagController(ITagService service) : ControllerBase
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
        var addedTag = await service.CreateTag(createTagRequestTo);
        return CreatedAtAction(nameof(GetTag), new { id = addedTag.id }, addedTag);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteTag(long id)
    {
        await service.DeleteTag(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<TagResponseTo>> UpdateTag(UpdateTagRequestTo updateTagRequestTo)
    {
        return Ok(await service.UpdateTag(updateTagRequestTo));
    }
}