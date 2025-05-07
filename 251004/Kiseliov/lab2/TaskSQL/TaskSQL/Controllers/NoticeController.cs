using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;
using TaskSQL.Services.Interfaces;

namespace TaskSQL.Controllers;

[Route("api/v{version:apiVersion}/notices")]
[ApiVersion("1.0")]
[ApiController]
public class NoticeController(INoticeService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Notice>> GetNotice(long id)
    {
        return Ok(await service.GetNoticeById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Notice>>> GetNotices()
    {
        return Ok(await service.GetNotices());
    }

    [HttpPost]
    public async Task<ActionResult<NoticeResponseTo>> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo)
    {
        var addedNotice = await service.CreateNotice(createNoticeRequestTo);
        return CreatedAtAction(nameof(GetNotice), new { id = addedNotice.id }, addedNotice);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteNotice(long id)
    {
        await service.DeleteNotice(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<NoticeResponseTo>> UpdateNotice(UpdateNoticeRequestTo updateNoticeRequestTo)
    {
        return Ok(await service.UpdateNotice(updateNoticeRequestTo));
    }
}