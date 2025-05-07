using Confluent.Kafka.Admin;
using Confluent.Kafka;

namespace DistComp.Properties {
    public static class KafkaExtensions {
        public static async Task CreateTopicIfNotExistsAsync(string bootstrapServices, string topicName) {
            var config = new AdminClientConfig {
                BootstrapServers = bootstrapServices
            };

            using var adminClient = new AdminClientBuilder(config).Build();

            var metadata = adminClient.GetMetadata(TimeSpan.FromSeconds(10));
            if (metadata.Topics.Any(t => t.Topic == topicName)) {
                return;
            }

            await adminClient.CreateTopicsAsync(new List<TopicSpecification> {
                new TopicSpecification
                {
                    Name = topicName,
                    NumPartitions = 1,
                    ReplicationFactor = 1,
                    Configs = new Dictionary<string, string>
                    {
                        {"retention.ms", "604800000"} // 7 days
                    }
                }
            });
        }
    }
}
