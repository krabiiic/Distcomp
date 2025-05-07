using Asp.Versioning;
using Discussion.Dto.Request;
using Discussion.Dto.Response;
using Discussion.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
namespace Discussion.Controllers;

[Route("api/v{version:apiVersion}/notices")]
[ApiVersion("1.0")]
[ApiController]
public class NoticeController(INoticeService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<NoticeResponseTo>> GetNotice(long id)
    {
        return Ok(await service.GetNoticeById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<NoticeResponseTo>>> GetNotices()
    {
        return Ok(await service.GetNotices());
    }

    [HttpPost]
    public async Task<ActionResult<NoticeResponseTo>> CreateNotice(NoticeRequestTo noticeRequestTo)
    {
        NoticeResponseTo addedNotice = await service.CreateNotice(noticeRequestTo);
        return CreatedAtAction(nameof(GetNotice), new { id = addedNotice.Id }, addedNotice);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteNotice(long id)
    {
        await service.DeleteNotice(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<NoticeResponseTo>> UpdateNotice(NoticeRequestTo updateNoticeRequestTo)
    {
        return Ok(await service.UpdateNotice(updateNoticeRequestTo));
    }
}