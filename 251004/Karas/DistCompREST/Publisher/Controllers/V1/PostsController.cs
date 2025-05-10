using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients;
using Publisher.HttpClients.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class PostsController : ControllerBase
{
    private readonly IDiscussionClient _postClient;

    public PostsController(IDiscussionClient postClient)
    {
        _postClient = postClient;
    }

    [HttpGet]
    public async Task<IActionResult> GetPosts()
    {
        var posts = await _postClient.GetPostsAsync();
        return Ok(posts);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetPostById(long id)
    {
        var post = await _postClient.GetPostByIdAsync(id);
        return Ok(post);
    }

    [HttpPost]
    public async Task<IActionResult> CreatePost([FromBody] PostRequestDTO post)
    {
        var createdPost = await _postClient.CreatePostAsync(post);
        return CreatedAtAction(nameof(CreatePost), new { id = createdPost.Id }, createdPost);
    }

    [HttpPut]
    public async Task<IActionResult> UpdatePost([FromBody] PostRequestDTO post)
    {
        var updatedPost = await _postClient.UpdatePostAsync(post);
        return Ok(updatedPost);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePost(long id)
    {
        await _postClient.DeletePostAsync(id);
        return NoContent();
    }
}