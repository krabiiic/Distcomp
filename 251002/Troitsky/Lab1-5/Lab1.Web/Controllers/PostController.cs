using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Abstractions;
using System.Text.Json;
using StackExchange.Redis;

namespace Lab1.Web.Controllers
{

    [ApiController]
    [Route("api/v1.0/posts")]
    public class PostController : ControllerBase
    {
        private readonly HttpClient _httpClient;
        private readonly IProducer _producer;
        private readonly IConsumer _consumer;
        private readonly INewsService _newsService;
        private readonly IDatabase _db;
        public PostController(HttpClient httpClient, IProducer producer, IConsumer consumer,INewsService service, IConnectionMultiplexer multiplexer)
        {
            _httpClient = httpClient;
            _producer = producer;
            _consumer = consumer;
            _newsService = service;
            _db = multiplexer.GetDatabase();
        }

        [HttpPost]
        // Создается объект PostRequest с уникальным ID
        public async Task<IActionResult> Create([FromBody] PostRequestTo dto)
        {
            var res = _newsService.GetNews(dto.NewsId); 
            if (res == null)
            {
                return NotFound();
            }

            var msg = new PostRequest(Guid.NewGuid().ToString(), "Create", JsonSerializer.Serialize(dto));
            await _producer.SendMessageAsync(dto.NewsId.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");
            return response.Data == null ? StatusCode(response.StatusCode): StatusCode(response.StatusCode, response.Data);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(ulong id)
        {
            var msg = new PostRequest(Guid.NewGuid().ToString(), "Delete", id.ToString());

            await _producer.SendMessageAsync(id.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");

            if (_db.KeyExists(id.ToString()))
            {
                _db.KeyDelete(id.ToString());
            }

            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(ulong id)
        {

            if (_db.KeyExists(id.ToString()))
            {
                string value = _db.StringGet(id.ToString())!;
                return StatusCode(200, value);
            }
            else
            {
                Console.WriteLine($"Not found in redis:{id.ToString()}");
            }

            var msg = new PostRequest(Guid.NewGuid().ToString(), "Get", id.ToString());

            await _producer.SendMessageAsync(id.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            // WriteIndented = true - делает читабельным добавлением отступов
            if (response == null)
                return StatusCode(202, "Processing...");
            if (response.Data != null && response.StatusCode >= 200 && response.StatusCode <= 299)
            {
                if (_db.StringSet(id.ToString(), response.Data))
                {
                    Console.WriteLine($"Response: {response.Data}");
                    Console.WriteLine($"[New record with {id} id has been added to redis:\r\n {_db.StringGet(id.ToString())}");
                }
            }
            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] PostUpdateRequest dto)
        {
            var msg = new PostRequest(Guid.NewGuid().ToString(), "Update", JsonSerializer.Serialize(dto));
            await _producer.SendMessageAsync(dto.NewsId.ToString(), msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");
            if (_db.KeyExists(dto.Id.ToString()))
            {
                _db.KeyDelete(dto.Id.ToString());
            }

            if (response.Data != null && response.StatusCode >= 200 && response.StatusCode <= 299)
            {
                await _db.StringSetAsync(dto.Id.ToString(), response.Data);
            }

            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var msg = new PostRequest(Guid.NewGuid().ToString(), "GetAll");
            await _producer.SendMessageAsync(msg.Id, msg);
            var response = await _consumer.WaitForResponseAsync(msg.Id, TimeSpan.FromSeconds(10));
            if (response == null)
                return StatusCode(202, "Processing...");
            return response.Data == null ? StatusCode(response.StatusCode) : StatusCode(response.StatusCode, response.Data);
        }
    }
}