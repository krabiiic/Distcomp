using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Publisher.HttpClients.Interfaces;

namespace Publisher.Controllers;

[ApiController]
[Route("api/v1.0/[controller]")]
public class MessagesController : ControllerBase
{
    private readonly IDiscussionClient _messageClient;
    public MessagesController(IDiscussionClient messageClient)
    {
        _messageClient = messageClient;
    }

    [HttpGet]
    public async Task<IActionResult> GetMessages()
    {
        var messages = await _messageClient.GetMessagesAsync();
        return Ok(messages);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMessageById(long id)
    {
        var message = await _messageClient.GetMessageByIdAsync(id);
        return Ok(message);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMessage([FromBody] MessageRequestDTO message)
    {
        var createdMessage = await _messageClient.CreateMessageAsync(message);
        return CreatedAtAction(nameof(CreateMessage), new { id = createdMessage.Id }, createdMessage);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMessage([FromBody] MessageRequestDTO message)
    {
        var updatedMessage = await _messageClient.UpdateMessageAsync(message);
        return Ok(updatedMessage);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMessage(long id)
    {
        await _messageClient.DeleteMessageAsync(id);
        return NoContent();
    }
}