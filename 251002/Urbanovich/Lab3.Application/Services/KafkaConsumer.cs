using AutoMapper;
using Confluent.Kafka;
using Lab3.Application.Contracts;
using Lab3.Application.Exceptions;
using Lab3.Core.Abstractions;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System.Text.Json;

namespace Lab3.Application.Services
{
    public class KafkaConsumer : IConsumer, IHostedService
    {
        private readonly IConsumer<string, string> _consumer;
        private readonly IProducer _producer;
        private readonly IMapper _mapper;
        private readonly IServiceScopeFactory _serviceScopeFactory;
        private CancellationTokenSource? _cts;
        private Task? _executingTask;

        public KafkaConsumer(IProducer producer, IMapper mapper, IServiceScopeFactory serviceScopeFactory)
        {
            var config = new ConsumerConfig
            {
                BootstrapServers = "kafka:9092",
                GroupId = "message-consumer-group",
                AutoOffsetReset = AutoOffsetReset.Earliest
            };

            _consumer = new ConsumerBuilder<string, string>(config).Build();
            _consumer.Subscribe("InTopic");
            _producer = producer;
            _mapper = mapper;
            _serviceScopeFactory = serviceScopeFactory;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _cts = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            _executingTask = Task.Run(() => StartConsuming(_cts.Token), _cts.Token);
            Console.WriteLine("Kafka consumer started.");
            return Task.CompletedTask;
        }

        public async Task StopAsync(CancellationToken cancellationToken)
        {
            if (_cts != null)
            {
                _cts.Cancel();
                _cts.Dispose();
            }

            if (_executingTask != null)
            {
                await _executingTask;
            }

            _consumer.Close();
            Console.WriteLine("Kafka consumer stopped.");
        }

        private async Task StartConsuming(CancellationToken cancellationToken)
        {
            try
            {
                while (!cancellationToken.IsCancellationRequested)
                {
                    try
                    {
                        var consumeResult = _consumer.Consume(cancellationToken);
                        var request = JsonSerializer.Deserialize<MessageRequest>(consumeResult.Message.Value);

                        Console.WriteLine($"Accepted request: {consumeResult.Message.Value}");

                        var response = await ProcessMessageAsync(request!);
                        await _producer.SendMessageAsync(request!.Id, response);
                        Console.WriteLine($"[MessageService KafkaConsumer] Отправлен ответ в Kafka: " +
                            $"{JsonSerializer.Serialize(response,new JsonSerializerOptions { WriteIndented=true})}");
                    }
                    catch (OperationCanceledException)
                    {
                        break; // Корректное завершение при отмене токена
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"Kafka consumer error: {ex.Message}");
                    }
                }
            }
            finally
            {
                _consumer.Close();
                Console.WriteLine("Kafka consumer closed.");
            }
        }

        private async Task<MessageResponse> ProcessMessageAsync(MessageRequest request)
        {
            using var scope = _serviceScopeFactory.CreateScope();
            var messageService = scope.ServiceProvider.GetRequiredService<IMessageService>();

            try
            {
                return request.Action switch
                {
                    "Create" => HandleCreate(request, messageService),
                    "Delete" => HandleDelete(request, messageService),
                    "Update" => HandleUpdate(request, messageService),
                    "Get" => HandleGet(request, messageService),
                    "GetAll" => HandleGetAll(request, messageService),
                    _ => new MessageResponse(request.Id, 500, "Unknown Action")
                };
            }
            catch (IncorrectDataException)
            {
                return new MessageResponse(request.Id, 400);
            }
            catch (IncorrectDatabaseException)
            {
                return new MessageResponse(request.Id, 403);
            }
            catch (Exception)
            {
                return new MessageResponse(request.Id, 500);
            }
        }

        private MessageResponse HandleCreate(MessageRequest request, IMessageService messageService)
        {
            var dto = JsonSerializer.Deserialize<MessageRequestTo>(request.Data!);
            var result = messageService.CreateMessage(_mapper.Map<Lab3.Core.Models.Message>(dto));
            return new MessageResponse(request.Id, 201, JsonSerializer.Serialize(result));
        }

        private MessageResponse HandleDelete(MessageRequest request, IMessageService messageService)
        {
            var id = JsonSerializer.Deserialize<ulong>(request.Data!);
            return messageService.DeleteMessage(id)
                ? new MessageResponse(request.Id, 204)
                : new MessageResponse(request.Id, 404);
        }

        private MessageResponse HandleUpdate(MessageRequest request, IMessageService messageService)
        {
            var dto = JsonSerializer.Deserialize<MessageUpdateRequest>(request.Data!);
            var result = messageService.UpdateMessage(_mapper.Map<Lab3.Core.Models.Message>(dto), dto!.Id);
            return new MessageResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }

        private MessageResponse HandleGet(MessageRequest request, IMessageService messageService)
        {
            var id = JsonSerializer.Deserialize<ulong>(request.Data!);
            var result = messageService.GetMessage(id);
            return new MessageResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }

        private MessageResponse HandleGetAll(MessageRequest request, IMessageService messageService)
        {
            var result = messageService.GetAllMessages();
            return new MessageResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }
    }
}
