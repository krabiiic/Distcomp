using Application.DTO.Request;
using Application.DTO.Request.Editor;
using Application.DTO.Response;
using Application.DTO.Response.Editor;
using Application.Interfaces;
using Infrastructure.Repositories.Redis;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/editors")]
public class EditorController : ControllerBase
{
    private readonly IEditorService _editorService;
    private readonly IRedisCacheService _redis;
    private readonly string _cachePrefix = "editor";
    private readonly TimeSpan _cacheExpiration = TimeSpan.FromSeconds(10);
    public EditorController(IEditorService service, IRedisCacheService redis)
    {
        _editorService = service;
        _redis = redis;
    }

    [HttpGet]
    public async Task<IActionResult> GetAllEditors()
    {
        var editors = await _editorService.GetEditors(new EditorRequestToGetAll());
        return Ok(editors);
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetEditorById(long id)
    {
        var cachedResult = await _redis.GetCacheValueAsync<EditorResponseToGetById?>(
            $"{_cachePrefix}:{id}"
        );

        if (cachedResult is not null)
        {
            return StatusCode(StatusCodes.Status200OK, cachedResult);
        }

        EditorResponseToGetById editor = await _editorService.GetEditorById(new EditorRequestToGetById() {Id = id});
        
        await _redis.SetCacheValueAsync<EditorResponseToGetById>(
            $"{_cachePrefix}:{id}",
            editor,
            _cacheExpiration
        );
        return Ok(editor);
    }
    [HttpPost]
    public async Task<IActionResult> CreateEditor(EditorRequestToCreate editorRequestToCreate)
    {
        var editor = await _editorService.CreateEditor(editorRequestToCreate);
        return CreatedAtAction(nameof(GetEditorById), new { id = editor.Id }, editor);
    }
    
    [HttpPut]
    public async Task<IActionResult> UpdateEditor(EditorRequestToFullUpdate editorModel)
    {
        var editor = await _editorService.UpdateEditor(editorModel);
        
        var redisKey = $"{_cachePrefix}:{editor.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return Ok(editor);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteEditor(long id)
    {
        var editor = await _editorService.DeleteEditor(new EditorRequestToDeleteById(){Id = id});
        
        var redisKey = $"{_cachePrefix}:{editor.Id}";
        await _redis.RemoveCacheValueAsync(redisKey);
        
        return NoContent();
    }
}