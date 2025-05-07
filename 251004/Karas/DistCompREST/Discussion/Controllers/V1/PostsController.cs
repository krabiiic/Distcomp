using Discussion.DTO.Request;
using Discussion.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Discussion.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class PostsController : ControllerBase
{
    private readonly IPostService _postService;

    public PostsController(IPostService postService)
    {
        _postService = postService;
    }
    
    [HttpGet]
    public async Task<IActionResult> GetPosts()
    {
        var posts = await _postService.GetPostsAsync();
        return Ok(posts);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetPostById(long id)
    {
        var post = await _postService.GetPostByIdAsync(id);
        return Ok(post);
    }

    [HttpPost]
    public async Task<IActionResult> CreatePost([FromBody] PostRequestDTO post)
    {
        var createdPost = await _postService.CreatePostAsync(post);
        return CreatedAtAction(nameof(CreatePost), new { id = createdPost.Id }, createdPost);
    }

    [HttpPut]
    public async Task<IActionResult> UpdatePost([FromBody] PostRequestDTO post)
    {
        var updatedPost = await _postService.UpdatePostAsync(post);
        return Ok(updatedPost);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePost(long id)
    {
        await _postService.DeletePostAsync(id);
        return NoContent();
    }
}