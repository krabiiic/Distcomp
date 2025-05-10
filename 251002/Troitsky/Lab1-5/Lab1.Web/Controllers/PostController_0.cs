/*
using Lab1.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/posts")]
    public class PostController : ControllerBase
    {
        private IPostService _postService;
        private IMapper _mapper;
        public PostController(IPostService postService, IMapper mapper) => (_postService, _mapper) = (postService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] PostRequestTo dto)
        {
            var res = _postService.CreatePost(_mapper.Map<Post>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_postService.DeletePost(id))
                return StatusCode(204);
            return NotFound();
        }
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var post = _postService.GetPost(id);
            return StatusCode(200, post);
        }
        [HttpPut]
        public IActionResult Update([FromBody] PostUpdateRequest updPost)
        {
            var post = _postService.UpdatePost(_mapper.Map<Post>(updPost), updPost.Id);
            return StatusCode(200, post);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var posts = _postService.GetAllPosts();
            return StatusCode(200, posts);
        }
    }
}
*/