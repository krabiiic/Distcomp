using DistComp_1.DTO.RequestDTO;
using DistComp_1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DistComp_1.Controllers.V1;

[ApiController]
[Route("api/v1.0/[controller]")]
public class MessagesController : ControllerBase
{
    private readonly IMessageService _MessageService;

    public MessagesController(IMessageService MessageService)
    {
        _MessageService = MessageService;
    }

    [HttpGet]
    public async Task<IActionResult> GetMessages()
    {
        var Messages = await _MessageService.GetMessagesAsync();
        return Ok(Messages);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMessageById(long id)
    {
        var Message = await _MessageService.GetMessageByIdAsync(id);
        return Ok(Message);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMessage([FromBody] MessageRequestDTO Message)
    {
        var createdMessage = await _MessageService.CreateMessageAsync(Message);
        return CreatedAtAction(nameof(CreateMessage), new { id = createdMessage.Id }, createdMessage);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMessage([FromBody] MessageRequestDTO Message)
    {
        var updatedMessage = await _MessageService.UpdateMessageAsync(Message);
        return Ok(updatedMessage);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMessage(long id)
    {
        await _MessageService.DeleteMessageAsync(id);
        return NoContent();
    }
}