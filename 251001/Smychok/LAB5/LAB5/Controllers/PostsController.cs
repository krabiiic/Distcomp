using LAB5.Repositories;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using LAB5.DTOs;
using LAB5.Services;
using Microsoft.Extensions.Logging;
using StackExchange.Redis;

namespace LAB5.Controllers;

[Route("api/v1.0/[controller]")]
[ApiController]
public class PostsController : ControllerBase
{
    private readonly PostService _postService;
    private readonly ILogger<PostsController> _logger;
    private readonly IDatabase _redisDb;

    public PostsController(
        PostService postService,
        ILogger<PostsController> logger,
        IDatabase redisDb)
    {
        _postService = postService;
        _logger = logger;
        _redisDb = redisDb;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<PostResponseTo>>> GetPosts([FromQuery] QueryParams queryParams)
    {
        _logger.LogInformation("GetAllPosts request with Redis check simulation");
        var posts = await _postService.GetAllAsync(queryParams);
        return Ok(posts);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<PostResponseTo>> GetPost(int id)
    {
        _logger.LogInformation($"GetPost {id} request with Redis check simulation");
        var post = await _postService.GetByIdAsync(id);
        if (post == null) return NotFound();
        return Ok(post);
    }

    [HttpPost]
    public async Task<ActionResult<PostResponseTo>> CreatePost(PostRequestTo postRequest)
    {
        _logger.LogInformation("CreatePost request with Redis caching simulation");
        var post = await _postService.CreateAsync(postRequest);
        return CreatedAtAction(nameof(GetPost), new { id = post.Id }, post);
    }

    [HttpPut]
    public async Task<ActionResult<PostResponseTo>> UpdatePost(PostRequestTo postRequest)
    {
        _logger.LogInformation($"UpdatePost {postRequest.id} request with Redis cache update");
        var post = await _postService.UpdateAsync(postRequest);
        if (post == null) return NotFound();
        return Ok(post);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePost(int id)
    {
        _logger.LogInformation($"DeletePost {id} request with Redis cache invalidation");
        var isDeleted = await _postService.DeleteAsync(id);
        if (!isDeleted) return NotFound();
        return NoContent();
    }
}