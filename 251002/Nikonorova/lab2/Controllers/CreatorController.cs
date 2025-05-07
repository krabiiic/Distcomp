using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using lab2_jpa.Models;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Controllers;

[Route("api/v1.0/creators")]
[ApiController]
public class CreatorController(ICreatorService service) : ControllerBase
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
        var addedCreator = await service.CreateCreator(createCreatorRequestTo);
        return CreatedAtAction(nameof(GetCreator), new { id = addedCreator.id }, addedCreator);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteCreator(long id)
    {
        await service.DeleteCreator(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<CreatorResponseTo>> UpdateCreator(UpdateCreatorRequestTo updateCreatorRequestTo)
    {
        return Ok(await service.UpdateCreator(updateCreatorRequestTo));
    }
}