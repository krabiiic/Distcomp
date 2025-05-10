using Lab1.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/tags")]
    public class TagController : ControllerBase
    {
        private ITagService _tagService;
        private IMapper _mapper;
        public TagController(ITagService tagService, IMapper mapper) => (_tagService, _mapper) = (tagService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] TagRequestTo dto)
        {
            var res = _tagService.CreateTag(_mapper.Map<Tag>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_tagService.DeleteTag(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var tag = _tagService.GetTag(id);
            return StatusCode(200, tag);
        }
        [HttpPut]
        public IActionResult Update([FromBody] TagUpdateRequest updTag)
        {
            var tag = _tagService.UpdateTag(_mapper.Map<Tag>(updTag), updTag.Id);
            return StatusCode(200, tag);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var tags = _tagService.GetAllTags();
            return StatusCode(200, tags);
        }
    }
}
