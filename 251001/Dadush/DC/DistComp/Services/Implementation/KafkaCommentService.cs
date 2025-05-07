using DistComp.Models;
using Confluent.Kafka;
using RestKafkaApi;
using System.Text.Json;
using DistComp.Data;
using Confluent.Kafka.Admin;

namespace DistComp.Services.Implementation {
    public class KafkaCommentService : ICommentService, IDisposable {

        private string bootstrapServices;

        private IProducer<string, string> producer;
        private IConsumer<string, string> consumer;

        private string inTopic;
        private string outTopic;

        private TimeSpan timeout;

        private DCContext context;

        public KafkaCommentService(IConfiguration configuration, DCContext context) {
            bootstrapServices = configuration.GetConnectionString("kafka_bootstrap_services")!;

            var producerConfig = new ProducerConfig {
                BootstrapServers = configuration.GetConnectionString("kafka_bootstrap_services")
            };

            var consumerConfig = new ConsumerConfig {
                BootstrapServers = configuration.GetConnectionString("kafka_bootstrap_services"),
                GroupId = $"api-response-consumer",
                AutoOffsetReset = AutoOffsetReset.Earliest
            };

            producer = new ProducerBuilder<string, string>(producerConfig).Build();
            consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();

            inTopic = configuration["Kafka:InTopic"]!;
            outTopic = configuration["Kafka:OutTopic"]!;

            timeout = TimeSpan.Parse(configuration["Kafka:ResponseTimeout"]!);

            this.context = context;

            consumer.Subscribe(outTopic);
        }

        public async Task<IEnumerable<CommentOutDto>> GetAll() {
            await PostMessageAsync(new ApiRequest {
                Method = "GET",
                Path = "/comments",
                Body = ""
            });
            
            var response = await WaitForResponseAsync();
            response.EnsureSuccessStatusCode();
            return JsonSerializer.Deserialize<IEnumerable<CommentOutDto>>(response.Body)!;
        }

        public async Task<CommentOutDto?> Get(long id) {
            await PostMessageAsync(new ApiRequest {
                Method = "GET",
                Path = $"/comments/{id}",
                Body = ""
            });

            var response = await WaitForResponseAsync();
            if (response.StatusCode == 404) {
                return null;
            } else {
                response.EnsureSuccessStatusCode();
            }
            return JsonSerializer.Deserialize<CommentOutDto>(response.Body)!;
        }

        public async Task<CommentOutDto> Create(CommentInDto data) {
            var topic = await context.Topics.FindAsync(data.TopicId) ??
                throw new KeyNotFoundException("Topic with specified ID does not exist");

            await PostMessageAsync(new ApiRequest {
                Method = "POST",
                Path = $"/comments",
                Body = JsonSerializer.Serialize(data)
            });

            return new CommentOutDto {
                Id = -1,
                TopicId = data.TopicId,
                Content = data.Content,
            };
        }

        public async Task<CommentOutDto> Update(Comment data) {
            var topic = await context.Topics.FindAsync(data.TopicId);
            if (topic is null) {
                throw new KeyNotFoundException("Topic with specified ID does not exist");
            }

            await PostMessageAsync(new ApiRequest {
                Method = "PUT",
                Path = $"/comments",
                Body = JsonSerializer.Serialize(data)
            });

            var response = await WaitForResponseAsync();
            response.EnsureSuccessStatusCode();
            return JsonSerializer.Deserialize<CommentOutDto>(response.Body)!;
        }

        public async Task Delete(long id) {
            await PostMessageAsync(new ApiRequest {
                Method = "DELETE",
                Path = $"/comments/{id}",
                Body = ""
            });

            var response = await WaitForResponseAsync();
            response.EnsureSuccessStatusCode();
        }

        private async Task PostMessageAsync(ApiRequest request) {
            var message = new Message<string, string>() {
                Key = request.Id,
                Value = JsonSerializer.Serialize(request)
            };
            await producer.ProduceAsync(inTopic, message);
        }

        private async Task<ApiResponse> WaitForResponseAsync() {
            var result = consumer.Consume(timeout);
            return JsonSerializer.Deserialize<ApiResponse>(result.Message.Value)!;
        }

        public void Dispose() {
            producer?.Dispose();
            consumer?.Dispose();
        }
    }
}
