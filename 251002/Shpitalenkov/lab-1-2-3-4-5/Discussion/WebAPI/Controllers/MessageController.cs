using Core.DTO;
using Core.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("/api/v1.0/messages")]
public class MessageController (IMessageService _messageService) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAllMessages()
    {
        var message = await _messageService.GetMessages(new MessageRequestToGetAll());
        return Ok(message);
    }
        
    [HttpGet("{id:long}")]
    public async Task<IActionResult> GetMessageById(long id)
    {
        MessageResponseToGetById message = await _messageService.GetMessageById(new MessageRequestToGetById() {Id = id});
        return Ok(message);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMessage(MessageRequestToCreate messageRequestToCreate)
    {
        var message = await _messageService.CreateMessage(messageRequestToCreate);
        return CreatedAtAction(nameof(GetMessageById), new { id = message.Id }, message);
    }
        
    [HttpPut]
    public async Task<IActionResult> UpdateMessage(MessageRequestToFullUpdate messageModel)
    {
        var message = await _messageService.UpdateMessage(messageModel);
        return Ok(message);
    }
        
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteMessage(long id)
    {
        var message = await _messageService.DeleteMessage(new MessageRequestToDeleteById(){Id = id});
        return NoContent();
    }
}