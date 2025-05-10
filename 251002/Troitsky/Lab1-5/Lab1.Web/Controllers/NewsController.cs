using AutoMapper;
using Lab1.Application.Contracts;
using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Microsoft.AspNetCore.Mvc;

namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/news")]
    public class NewsController : ControllerBase
    {
        private INewsService _newsService;
        private IMapper _mapper;
        public NewsController(INewsService newsService, IMapper mapper) => (_newsService, _mapper) = (newsService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] NewsRequestTo dto)
        {
            var res = _newsService.CreateNews(_mapper.Map<News>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_newsService.DeleteNews(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var news = _newsService.GetNews(id);
            return StatusCode(200, news);
        }
        [HttpPut]
        public IActionResult Update([FromBody] NewsUpdateRequest updNews)
        {
            var news = _newsService.UpdateNews(_mapper.Map<News>(updNews), updNews.Id);
            return StatusCode(200, news);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var news = _newsService.GetAllNews();
            return StatusCode(200, news);
        }
    }
}
