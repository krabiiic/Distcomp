using DistComp.DTO.RequestDTO;
using DistComp.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class IssuesController : ControllerBase
{
    private readonly IIssueService _issueService;

    public IssuesController(IIssueService issueService)
    {
        _issueService = issueService;
    }

    [HttpGet]
    public async Task<IActionResult> GetIssues()
    {
        var issues = await _issueService.GetIssuesAsync();
        return Ok(issues);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetIssueById(long id)
    {
        var issue = await _issueService.GetIssueByIdAsync(id);
        return Ok(issue);
    }

    [HttpPost]
    public async Task<IActionResult> CreateIssue([FromBody] IssueRequestDTO issue)
    {
        var createdIssue = await _issueService.CreateIssueAsync(issue);
        return CreatedAtAction(nameof(CreateIssue), new { id = createdIssue.Id }, createdIssue);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateIssue([FromBody] IssueRequestDTO issue)
    {
        var updatedIssue = await _issueService.UpdateIssueAsync(issue);
        return Ok(updatedIssue);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteIssue(long id)
    {
        await _issueService.DeleteIssueAsync(id);
        return NoContent();
    }
}