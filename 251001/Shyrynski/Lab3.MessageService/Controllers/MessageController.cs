using Lab3.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab3.Application.Contracts;
using Lab3.Core.Models;
using AutoMapper;

namespace Lab3.MessageService.Controllers
{
    [ApiController]
    [Route("api/v1.0/messages")]
    public class MessageController : ControllerBase
    {
        private IMessageService _messageService;
        private IMapper _mapper;
        public MessageController(IMessageService messageService, IMapper mapper) => (_messageService, _mapper) = (messageService, mapper);

        [HttpPost]
        public IActionResult Create([FromBody] MessageRequestTo dto)
        {
            var res = _messageService.CreateMessage(_mapper.Map<Message>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_messageService.DeleteMessage(id))
                return StatusCode(204);
            return NotFound();
        }

        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var message = _messageService.GetMessage(id);
            if (message == null)
                return NotFound();
            return StatusCode(200, message);
        }
        [HttpPut]
        public IActionResult Update([FromBody] MessageUpdateRequest updMessage)
        {
            var message = _messageService.UpdateMessage(_mapper.Map<Message>(updMessage), updMessage.Id);
            return StatusCode(200, message);
        }
        [HttpGet]
        public IActionResult GetAll()
        {
            var messages = _messageService.GetAllMessages();
            return StatusCode(200, messages);
        }
    }
}
