using CassandraPosts.Models;
using CassandraPosts.Services;
using Microsoft.AspNetCore.Mvc;

namespace CassandraPosts.Controllers;

[ApiController]
[Route("api/v1.0/[controller]")]
public class PostsController : ControllerBase
{
    private readonly PostService _postService;

    public PostsController(PostService postService)
    {
        _postService = postService;
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Post post)
    {
        var createdPost = await _postService.CreateAsync(post);
        return CreatedAtAction(nameof(GetById), new { postId = createdPost.Id }, createdPost);
    }
    

    [HttpGet]
    public async Task<IActionResult> GetAll(int pageSize = 10)
    {
        var page = await _postService.GetAllAsync(pageSize);
        return Ok(page);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(Guid postId, [FromBody] Post post)
    {
        if (postId != post.Id) return BadRequest();
        await _postService.UpdateAsync(post);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(Guid postId)
    {
        await _postService.DeleteAsync(postId);
        return NoContent();
    }
    
    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id)
    {
        // if (!Guid.TryParse(id, out var guid))
        //     return BadRequest("Invalid GUID format");
        //
        // var post = await _postService.GetByIdAsync(guid);
        // return post == null ? NotFound() : Ok(post);
        var page = await _postService.GetAllAsync(10);
        return Ok(page);
    }
}