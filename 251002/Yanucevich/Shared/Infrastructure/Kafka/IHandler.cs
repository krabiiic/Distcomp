using Confluent.Kafka;

namespace Shared.Infrastructure.Kafka;

public interface IHandler<TKey, TValue>
{
    Task Handle(IReadOnlyCollection<ConsumeResult<TKey, TValue>> messages, CancellationToken token);
}
