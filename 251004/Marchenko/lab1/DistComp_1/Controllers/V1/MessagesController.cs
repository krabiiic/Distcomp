using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class MessagesController : ControllerBase
{
    private readonly IMessageService _messageService;

    public MessagesController(IMessageService messageService)
    {
        _messageService = messageService;
    }

    [HttpGet]
    public async Task<IActionResult> GetMessages()
    {
        var messages = await _messageService.GetMessagesAsync();
        return Ok(messages);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMessageById(long id)
    {
        var message = await _messageService.GetMessageByIdAsync(id);
        return Ok(message);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMessage([FromBody] MessageRequestDTO message)
    {
        var createdMessage = await _messageService.CreateMessageAsync(message);
        return CreatedAtAction(nameof(CreateMessage), new { id = createdMessage.Id }, createdMessage);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMessage([FromBody] MessageRequestDTO message)
    {
        var updatedMessage = await _messageService.UpdateMessageAsync(message);
        return Ok(updatedMessage);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMessage(long id)
    {
        await _messageService.DeleteMessageAsync(id);
        return NoContent();
    }
}