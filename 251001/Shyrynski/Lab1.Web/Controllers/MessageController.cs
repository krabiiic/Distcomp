using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Abstractions;
using System.Text.Json;
using StackExchange.Redis;
using Microsoft.AspNetCore.Server.Kestrel.Core.Features;

namespace Lab1.Web.Controllers
{
    [ApiController]
    [Route("api/v1.0/messages")]
    public class MessageController : ControllerBase
    {
        private readonly HttpClient _httpClient;
        private readonly IProducer _producer;
        private readonly IConsumer _consumer;
        private readonly IIssueService _issueService;
        private readonly IDatabase _db;
        public MessageController(HttpClient httpClient, IProducer producer, IConsumer consumer, IIssueService service, IConnectionMultiplexer multiplexer)
        {
            _httpClient = httpClient;
            _producer = producer;
            _consumer = consumer;
            _issueService = service;
            _db = multiplexer.GetDatabase();
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] MessageRequestTo dto)
        {
            var res = _issueService.GetIssue(dto.IssueId);
            if (res == null)
            {
                return NotFound();
            }

            var msg = new MessageRequest(Guid.NewGuid().ToString(), "Create", JsonSerializer.Serialize(dto));
            await _producer.SendMessageAsync(dto.IssueId.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");
            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(ulong id)
        {
            var msg = new MessageRequest(Guid.NewGuid().ToString(), "Delete", id.ToString());

            await _producer.SendMessageAsync(id.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");

            if (_db.KeyExists(id.ToString()))
            {
                Console.WriteLine($"{id} was removed from redis");
                _db.KeyDelete(id.ToString());
            }

            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(ulong id)
        {

            if (_db.KeyExists(id.ToString()))
            {
                Console.WriteLine($"[GET] Found something with {id} in redis:\r\n {_db.StringGet(id.ToString())}");
                string value = _db.StringGet(id.ToString())!;
                Console.WriteLine($"{value}");
                return StatusCode(200, value);
            }

            var msg = new MessageRequest(Guid.NewGuid().ToString(), "Get", id.ToString());

            await _producer.SendMessageAsync(id.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");

            if (response.Data != null && response.StatusCode >= 200 && response.StatusCode <= 299)
            {
                if (_db.StringSet(id.ToString(), response.Data))
                {
                    Console.WriteLine($"Response itself: {response.Data}");
                    Console.WriteLine($"[GET] New record with {id} id has been added to redis:\r\n {_db.StringGet(id.ToString())}");
                }
            }

            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] MessageUpdateRequest dto)
        {
            var msg = new MessageRequest(Guid.NewGuid().ToString(), "Update", JsonSerializer.Serialize(dto));

            await _producer.SendMessageAsync(dto.IssueId.ToString(), msg);

            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");

            if (_db.KeyExists(dto.Id.ToString()))
            {
                Console.WriteLine($"{dto.Id} was removed from redis");
                _db.KeyDelete(dto.Id.ToString());
            }


            if (response.Data != null && response.StatusCode >= 200 && response.StatusCode <= 299)
            {
                await _db.StringSetAsync(dto.Id.ToString(), response.Data);
                Console.WriteLine($"[PUT] New record with {dto.Id} id has been added to redis:\r\n {_db.StringGet(dto.Id.ToString())}");
            }


            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var msg = new MessageRequest(Guid.NewGuid().ToString(), "GetAll");
            await _producer.SendMessageAsync(msg.Id, msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");
            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }
    }
}