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
            // для создания областей видимости сервисов, чтобы избежать проблем с многопоточностью
            _serviceScopeFactory = serviceScopeFactory;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _cts = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            _executingTask = Task.Run(() => StartConsuming(_cts.Token), _cts.Token);
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
                        var request = JsonSerializer.Deserialize<PostRequest>(consumeResult.Message.Value);

                        var response = await ProcessMessageAsync(request!);
                        await _producer.SendMessageAsync(request!.Id, response);
                    }
                    catch (OperationCanceledException)
                    {
                        break; 
                    }
                    catch (Exception ex)
                    {
                    }
                }
            }
            finally
            {
                _consumer.Close();
            }
        }

        private async Task<PostResponse> ProcessMessageAsync(PostRequest request)
        {
            using var scope = _serviceScopeFactory.CreateScope();
            var postService = scope.ServiceProvider.GetRequiredService<IPostService>();

            try
            {
                return request.Action switch
                {
                    "Create" => HandleCreate(request, postService),
                    "Delete" => HandleDelete(request, postService),
                    "Update" => HandleUpdate(request, postService),
                    "Get" => HandleGet(request, postService),
                    "GetAll" => HandleGetAll(request, postService),
                    _ => new PostResponse(request.Id, 500, "Unknown Action")
                };
            }
            catch (IncorrectDataException)
            {
                return new PostResponse(request.Id, 400);
            }
            catch (IncorrectDatabaseException)
            {
                return new PostResponse(request.Id, 403);
            }
            catch (Exception)
            {
                return new PostResponse(request.Id, 500);
            }
        }

        private PostResponse HandleCreate(PostRequest request, IPostService postService)
        {
            var dto = JsonSerializer.Deserialize<PostRequestTo>(request.Data!);
            try
            {
                var result = postService.CreatePost(_mapper.Map<Lab3.Core.Models.Post>(dto));
                return new PostResponse(request.Id, 201, JsonSerializer.Serialize(result));
            }
            catch (Exception ex)
            {
                throw ex;
            }

           
        }

        private PostResponse HandleDelete(PostRequest request, IPostService postService)
        {
            var id = JsonSerializer.Deserialize<ulong>(request.Data!);
            return postService.DeletePost(id)
                ? new PostResponse(request.Id, 204)
                : new PostResponse(request.Id, 404);
        }

        private PostResponse HandleUpdate(PostRequest request, IPostService postService)
        {
            var dto = JsonSerializer.Deserialize<PostUpdateRequest>(request.Data!);
            var result = postService.UpdatePost(_mapper.Map<Lab3.Core.Models.Post>(dto), dto!.Id);
            return new PostResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }

        private PostResponse HandleGet(PostRequest request, IPostService postService)
        {
            var id = JsonSerializer.Deserialize<ulong>(request.Data!);
            var result = postService.GetPost(id);
            return new PostResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }

        private PostResponse HandleGetAll(PostRequest request, IPostService postService)
        {
            var result = postService.GetAllPosts();
            return new PostResponse(request.Id, 200, JsonSerializer.Serialize(result));
        }
    }
}
