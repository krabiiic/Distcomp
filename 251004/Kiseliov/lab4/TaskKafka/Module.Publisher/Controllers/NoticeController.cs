using System.Globalization;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Publisher.Services.Interfaces;
namespace Publisher.Controllers;

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
    public async Task<ActionResult<NoticeResponseTo>> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo)
    {
        var country = GetCountryFromRequest();
        NoticeResponseTo addedNotice = await service.CreateNotice(createNoticeRequestTo, country);
        return CreatedAtAction(nameof(GetNotice), new
        {
            id = addedNotice.Id
        }, addedNotice);
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
        var country = GetCountryFromRequest();
        return Ok(await service.UpdateNotice(updateNoticeRequestTo, country));
    }

    private string GetCountryFromRequest()
    {
        var lang = Request.GetTypedHeaders().AcceptLanguage
            .MaxBy(x => x.Quality ?? 1)
            ?.Value.ToString();

        return string.IsNullOrEmpty(lang) ? "Unknown" : new RegionInfo(lang).TwoLetterISORegionName;
    }
}
