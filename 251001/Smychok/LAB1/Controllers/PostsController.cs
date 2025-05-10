namespace LAB1.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using DTOs;
using Services;

[Route("api/v1.0/[controller]")]
[ApiController]
public class PostsController : ControllerBase
{
    private readonly PostService _postService;

    public PostsController(PostService postService)
    {
        _postService = postService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<PostResponseTo>>> GetPosts()
    {
        var posts = await _postService.GetAllAsync();
        return Ok(posts);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<PostResponseTo>> GetPost(int id)
    {
        var post = await _postService.GetByIdAsync(id);
        if (post == null)
        {
            return NotFound();
        }
        return Ok(post);
    }

    [HttpPost]
    public async Task<ActionResult<PostResponseTo>> CreatePost(PostRequestTo postRequest)
    {
        var post = await _postService.CreateAsync(postRequest);
        return CreatedAtAction(nameof(GetPost), new { id = post.Id }, post);
    }

    [HttpPut]
    public async Task<ActionResult<PostResponseTo>> UpdatePost(int id, PostRequestTo postRequest)
    {
        var post = await _postService.UpdateAsync(id, postRequest);
        if (post == null)
        {
            return NotFound();
        }
        return Ok(post);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePost(int id)
    {
        var isDeleted = await _postService.DeleteAsync(id);
        if (!isDeleted)
        {
            return NotFound(); // 404 Not Found
        }

        return NoContent(); // 204 No Content
    }
}