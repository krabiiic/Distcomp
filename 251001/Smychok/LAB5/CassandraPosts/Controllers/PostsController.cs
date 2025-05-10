using CassandraPosts.Models;
using CassandraPosts.Services;
using Microsoft.AspNetCore.Mvc;

namespace CassandraPosts.Controllers;

[ApiController]
[Route("api/v1.0/[controller]")]
public class PostsController : ControllerBase
{
    private readonly PostService _postService;
    private readonly IdMappingService _idMappingService;
    private readonly ILogger<PostsController> _logger; // Добавляем логгер

    public PostsController(PostService postService, IdMappingService idMappingService,
        ILogger<PostsController> logger)
    {
        _postService = postService;
        _idMappingService = idMappingService;
        _logger = logger; // Инициализируем логгер
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Post post)
    {
        var createdPost = await _postService.CreateAsync(post);
        return CreatedAtAction(nameof(GetById), new { postId = createdPost.id }, createdPost);
    }
    

    [HttpGet]
    public async Task<IActionResult> GetAll(int pageSize = 10)
    {
        var page = await _postService.GetAllAsync(pageSize);
        return Ok(page);
    }
    

    [HttpPut]
    public async Task<IActionResult> Update([FromBody] PostUpdateDto postUpdate)
    {
        //if (postId != post.Id) return BadRequest();
        _idMappingService.TryGetMapping(postUpdate.id.ToString(), out var _cassandraId);
        var post = new Post();
        post.id = _cassandraId;
        post.IssueId = postUpdate.IssueId;
        post.Content = postUpdate.Content;
        await _postService.UpdateAsync(post);
        return Ok(new {
            Id = postUpdate.id,
            post.Content,
            post.IssueId,
        });
    }
    
    public class PostUpdateDto
    {
        public int id { get; set; }
        public int IssueId { get; set; }
        public string Content { get; set; }
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
        _idMappingService.DebugLogMappings();
        
        if (!_idMappingService.TryGetMapping(id, out var cassandraId))
        {
            _logger.LogWarning($"No mapping found for postId: {id}");
            return NotFound();
        }

        _logger.LogInformation($"Looking for Cassandra ID: {cassandraId}");
        
        var post = await _postService.GetByIdAsync(cassandraId);
        if (post == null)
        {
            _logger.LogError($"Post with Cassandra ID {cassandraId} not found in DB");
            return NotFound();
        }

        return Ok(new {
            Id = Int32.Parse(id),
            post.Content,
            post.IssueId,
            post.State
        });
    }
    
    
}