using Microsoft.AspNetCore.Mvc;
using lab2_jpa.Models;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Controllers;

[Route("api/v1.0/tags")]
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