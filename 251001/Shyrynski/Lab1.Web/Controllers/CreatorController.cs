using Lab1.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
using System.Text.Json;
namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/creators")]
    public class CreatorController : ControllerBase
    {
        private ICreatorService _creatorService;
        private IMapper _mapper;
        public CreatorController(ICreatorService creatorService, IMapper mapper) => (_creatorService, _mapper) = (creatorService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] CreatorRequestTo dto)
        {
            var res = _creatorService.CreateCreator(_mapper.Map<Creator>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_creatorService.DeleteCreator(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var creator = _creatorService.GetCreator(id);
            return StatusCode(200, creator);
        }
        [HttpPut]
        public IActionResult Update([FromBody] CreatorUpdateRequest updCreator)
        {
            var creator = _creatorService.UpdateCreator(_mapper.Map<Creator>(updCreator), updCreator.Id);
            return StatusCode(200, creator);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var creators = _creatorService.GetAllCreators();
            return StatusCode(200, creators);
        }
    }
}
