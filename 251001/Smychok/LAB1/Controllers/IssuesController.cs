namespace LAB1.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using DTOs;
using Services;

[Route("api/v1.0/[controller]")]
[ApiController]
public class IssuesController : ControllerBase
{
    private readonly IssueService _issueService;

    public IssuesController(IssueService issueService)
    {
        _issueService = issueService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<IssueResponseTo>>> GetIssues()
    {
        var issues = await _issueService.GetAllAsync();
        return Ok(issues);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<IssueResponseTo>> GetIssue(int id)
    {
        var issue = await _issueService.GetByIdAsync(id);
        if (issue == null)
        {
            return NotFound();
        }
        return Ok(issue);
    }

    [HttpPost]
    public async Task<ActionResult<IssueResponseTo>> CreateIssue(IssueRequestTo issueRequest)
    {
        try
        {
            var issue = await _issueService.CreateAsync(issueRequest);
            return CreatedAtAction(nameof(GetIssue), new { id = issue.Id }, issue);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(/*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return Conflict(/*ex.Message*/); // 409 Conflict
        }
    }

    [HttpPut]
    public async Task<ActionResult<IssueResponseTo>> UpdateIssue(int id, IssueRequestTo issueRequest)
    {
        try
        {
            var issue = await _issueService.UpdateAsync(id, issueRequest);
            if (issue == null)
            {
                return NotFound();
            }

            return Ok(issue);
        }
        catch (ArgumentException ex)
        {
            return BadRequest( /*ex.Message*/); // 400 Bad Request
        }
        catch (InvalidOperationException ex)
        {
            return Conflict( /*ex.Message*/); // 409 Conflict
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteIssue(int id)
    {
        var isDeleted = await _issueService.DeleteAsync(id);
        if (!isDeleted)
        {
            return NotFound(); // 404 Not Found
        }

        return NoContent(); // 204 No Content
    }
}