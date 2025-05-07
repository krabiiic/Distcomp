using Confluent.Kafka;
using Discussion.Data;
using Discussion.Models;
using RestKafkaApi;
using System.Text.Json;

namespace Discussion.Services.Background {
    public class KafkaBackgroundService : BackgroundService {

        private ICommentService commentService;
        private ICommentRepository repository;

        private ILogger<KafkaBackgroundService> logger;

        private IConsumer<string, string> consumer;
        private IProducer<string, string> producer;

        private string inTopic;
        private string outTopic;

        public KafkaBackgroundService(
            ICommentService commentService,
            ICommentRepository repository,
            IConfiguration configuration,
            ILogger<KafkaBackgroundService> logger
        ) {
            this.commentService = commentService;
            this.repository = repository;
            this.logger = logger;

            var config = new ConsumerConfig {
                BootstrapServers = configuration.GetConnectionString("kafka_bootstrap_services"),
                GroupId = "api-request-consumer",
                AutoOffsetReset = AutoOffsetReset.Earliest,
            };
            var producerConfig = new ProducerConfig {
                BootstrapServers = configuration.GetConnectionString("kafka_bootstrap_services"),
            };

            consumer = new ConsumerBuilder<string, string>(config).Build();
            producer = new ProducerBuilder<string, string>(producerConfig).Build();

            inTopic = configuration["Kafka:InTopic"]!;
            outTopic = configuration["Kafka:OutTopic"]!;
        }

        protected override Task ExecuteAsync(CancellationToken stoppingToken) {
            return Task.Run(async () => {
                consumer.Subscribe(inTopic);

                while (!stoppingToken.IsCancellationRequested) {
                    try {
                        var result = consumer.Consume(stoppingToken);
                        var request = JsonSerializer.Deserialize<ApiRequest>(result.Message.Value)!;

                        var response = await ProcessRequestAsync(request);
                        await producer.ProduceAsync(outTopic, new Message<string, string> {
                            Key = response.Id,
                            Value = JsonSerializer.Serialize(response)
                        }, stoppingToken);
                    } catch (ConsumeException ex) {
                        logger.LogError("Consume error: {0}", ex.Message);
                    } catch (OperationCanceledException) {
                        break;
                    } catch (Exception ex) {
                        logger.LogError("Internal error: {0}", ex.Message);
                    }
                }
            }, stoppingToken);
        }

        private async Task<ApiResponse> ProcessRequestAsync(ApiRequest request) {
            if (!request.Path.StartsWith("/comments")) {
                return BadRequest(request.Id);
            }
            
            long? id = ExtractIdFromPath(request.Path);

            switch (request.Method) {
            case "GET": {
                try {
                    if (id.HasValue) {
                        // GET */{id}
                        var comment = await commentService.GetAsync(id.Value);
                        if (comment != null) {
                            return new ApiResponse {
                                Id = request.Id,
                                StatusCode = 200,
                                Body = JsonSerializer.Serialize(comment)
                            };
                        } else {
                            return NotFound(request.Id);
                        }
                    } else {
                        // GET *
                        var comments = await commentService.GetAllAsync();
                        return new ApiResponse {
                            Id = request.Id,
                            StatusCode = 200,
                            Body = JsonSerializer.Serialize(comments)
                        };
                    }
                } catch (Exception) {
                    return NotFound(request.Id);
                }
                //break;
            }
            case "POST": {
                // POST *
                try {
                    var inDto = JsonSerializer.Deserialize<CommentInDto>(request.Body)!;
                    var comment = await commentService.CreateAsync(inDto);

                    return new ApiResponse {
                        Id = request.Id,
                        StatusCode = 201,
                        Body = JsonSerializer.Serialize(comment)
                    };
                } catch (Exception) {
                    return StatusCode(request.Id, 403);
                }
                //break;
            }
            case "PUT": {
                try {
                    var inDto = JsonSerializer.Deserialize<Comment>(request.Body)!;
                    var comment = await commentService.UpdateAsync(inDto);

                    return new ApiResponse {
                        Id = request.Id,
                        StatusCode = 200,
                        Body = JsonSerializer.Serialize(comment)
                    };
                } catch (KeyNotFoundException) {
                    return NotFound(request.Id);
                } catch (Exception) {
                    return StatusCode(request.Id, 500);
                }
                //break;
            }
            case "DELETE":
                try {
                    if (id.HasValue) {
                        await commentService.DeleteAsync(id.Value);

                        return new ApiResponse {
                            Id = request.Id,
                            StatusCode = 204,
                            Body = ""
                        };
                    } else {
                        return BadRequest(request.Id);
                    }
                } catch (KeyNotFoundException) {
                    return NotFound(request.Id);
                } catch (Exception) {
                    return StatusCode(request.Id, 500);
                }
                //break;
            default:
                return BadRequest(request.Id);
            }
        }

        private long? ExtractIdFromPath(ReadOnlySpan<char> path) {
            const string prefix = "/comments/";
            if (!path.StartsWith(prefix)) return null;

            var remaining = path.Slice(prefix.Length);
            if (remaining.IsEmpty) return null;

            if (long.TryParse(remaining, out long result)) {
                return result;
            } else {
                return null;
            }
        }

        private ApiResponse BadRequest(string id) {
            return new ApiResponse {
                Id = id,
                StatusCode = 400,
                Body = ""
            };
        }

        private ApiResponse NotFound(string id) {
            return new ApiResponse {
                Id = id,
                StatusCode = 404,
                Body = ""
            };
        }

        private ApiResponse StatusCode(string id, int statusCode) {
            return new ApiResponse {
                Id = id,
                StatusCode = statusCode,
                Body = ""
            };
        }

        public override void Dispose() {
            consumer?.Dispose();
            producer?.Dispose();
            base.Dispose();
        }
    }
}
