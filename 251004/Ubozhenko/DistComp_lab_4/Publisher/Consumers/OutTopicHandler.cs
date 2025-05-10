using Messaging;
using Messaging.Consumer.Interfaces;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Consumers;

public class OutTopicHandler : IKafkaHandler<string, KafkaMessage<ReactionResponseDTO>>
{
    public async Task HandleAsync(string key, KafkaMessage<ReactionResponseDTO> value)
    {
    }
}