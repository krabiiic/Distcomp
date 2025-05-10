using AutoMapper;
using Lab1.Application.Contracts;
using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Microsoft.AspNetCore.Mvc;

namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/issues")]
    public class IssueController : ControllerBase
    {
        private IIssueService _issueService;
        private IMapper _mapper;
        public IssueController(IIssueService issueService, IMapper mapper) => (_issueService, _mapper) = (issueService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] IssueRequestTo dto)
        {
            var res = _issueService.CreateIssue(_mapper.Map<Issue>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_issueService.DeleteIssue(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var issue = _issueService.GetIssue(id);
            return StatusCode(200, issue);
        }
        [HttpPut]
        public IActionResult Update([FromBody] IssueUpdateRequest updIssue)
        {
            var issue = _issueService.UpdateIssue(_mapper.Map<Issue>(updIssue), updIssue.Id);
            return StatusCode(200, issue);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var issues = _issueService.GetAllIssues();
            return StatusCode(200, issues);
        }
    }
}
