using Confluent.Kafka;
using CassandraPosts.Models;
using CassandraPosts.Repositories;
using System.Text.Json;

namespace CassandraPosts.Services;

public class KafkaConsumerService : BackgroundService
{
    private readonly IConsumer<Ignore, string> _consumer;
    private readonly IPostRepository _postRepository;
    private readonly ILogger<KafkaConsumerService> _logger;
    private readonly IProducer<Null, string> _producer;
    private readonly IdMappingService _idMappingService;
    
    public KafkaConsumerService(
        IPostRepository postRepository,
        IProducer<Null, string> producer,
        ILogger<KafkaConsumerService> logger,
        IdMappingService idMappingService)
    {
        _postRepository = postRepository;
        _producer = producer;
        _logger = logger;
        _idMappingService = idMappingService;

        var config = new ConsumerConfig
        {
            BootstrapServers = "localhost:9092",
            GroupId = "discussion-group",
            AutoOffsetReset = AutoOffsetReset.Earliest
        };
        _consumer = new ConsumerBuilder<Ignore, string>(config).Build();
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        _consumer.Subscribe("InTopic");

        while (!stoppingToken.IsCancellationRequested)
        {
            try
            {
                var consumeResult = _consumer.Consume(stoppingToken);
                _logger.LogInformation($"Received message: {consumeResult.Message.Value}");
                
                var message = JsonSerializer.Deserialize<KafkaMessage>(consumeResult.Message.Value);

                if (message == null) continue;

                switch (message.Action)
                {
                    case "Create":
                        await HandleCreate(message);
                        break;
                    case "Update":
                        await HandleUpdate(message);
                        break;
                    case "Delete":
                        await HandleDelete(message);
                        break;
                }
            }
            catch (OperationCanceledException)
            {
                // Игнорируем, так как это ожидаемо при остановке
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error processing Kafka message: {ex.Message}");
            }
        }

        _consumer.Close();
    }

    private async Task HandleCreate(KafkaMessage message)
    {
        try
        {
            _idMappingService.DebugLogMappings(); // Логируем текущие маппинги
        
            var status = ContainsStopWords(message.Content) ? "DECLINE" : "APPROVE";
            var newId = Guid.NewGuid();
            _logger.LogInformation($"Creating new post with Cassandra ID: {newId}");
        
            _idMappingService.AddMapping(message.PostId, newId);
            _idMappingService.DebugLogMappings(); // Логируем после добавления
            

            var post = new Post
            {
                id = newId,
                IssueId = message.IssueId,
                Content = message.Content,
                State = status
            };

            await _postRepository.CreateAsync(post);

            // Отправляем ответ с обоими ID
            var response = new
            {
                PostId = message.PostId,
                CassandraId = newId,
                Status = status
            };
            
            await _producer.ProduceAsync("OutTopic", new Message<Null, string>
            {
                Value = JsonSerializer.Serialize(response)
            });
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error in HandleCreate: {ex}");
        }
    }

    private async Task HandleUpdate(KafkaMessage message)
    {
        try
        {
            if (_idMappingService.TryGetMapping(message.PostId, out var cassandraId))
            {
                var post = await _postRepository.GetByIdAsync(cassandraId);
                if (post != null)
                {
                    post.Content = message.Content;
                    post.State = "PENDING";
                    await _postRepository.UpdateAsync(post);
                }
            }
            else
            {
                _logger.LogWarning($"No mapping found for PostId: {message.PostId}");
            }
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error handling Update message: {ex.Message}");
        }
    }

    private async Task HandleDelete(KafkaMessage message)
    {
        try
        {
            if (_idMappingService.TryGetMapping(message.PostId, out var cassandraId))
            {
                await _postRepository.DeleteAsync(cassandraId);
                _idMappingService.RemoveMapping(message.PostId);
                _logger.LogInformation($"Deleted post with ID: {cassandraId}");
            }
            else
            {
                _logger.LogWarning($"No mapping found for PostId: {message.PostId}");
            }
        }
        catch (Exception ex)
        {
            _logger.LogError($"Error handling Delete message: {ex.Message}");
        }
    }

    private bool ContainsStopWords(string content)
    {
        if (string.IsNullOrEmpty(content)) return false;
        
        var stopWords = new[] { "badword", "спам", "реклама" };
        return stopWords.Any(word => 
            content.Contains(word, StringComparison.OrdinalIgnoreCase));
    }
}

public class KafkaMessage
{
    public string PostId { get; set; }  // Теперь всегда string
    public int IssueId { get; set; }
    public string Content { get; set; }
    public string Action { get; set; }
}