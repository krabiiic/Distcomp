using Discussion.DTO.Request;
using Discussion.DTO.Response;
using Messaging;
using Messaging.Consumer.Interfaces;

namespace Discussion.Consumers;

public class InTopicHandler : IKafkaHandler<string, KafkaMessage<ReactionRequestDTO>>
{
    public async Task HandleAsync(string key, KafkaMessage<ReactionRequestDTO> value)
    {
    }
}