using System.Collections.Concurrent;

namespace Core.Kafka;

public class KafkaResponseDispatcher<TResponse>
{
    private readonly ConcurrentDictionary<
        string,
        TaskCompletionSource<TResponse>
    > _pendingRequests = new ConcurrentDictionary<string, TaskCompletionSource<TResponse>>();

    public void RegisterRequest(string correlationId, TaskCompletionSource<TResponse> tcs)
    {
        _pendingRequests[correlationId] = tcs;
    }

    public bool TryCompleteRequest(string correlationId, TResponse response)
    {
        if (_pendingRequests.TryRemove(correlationId, out var tcs))
        {
            tcs.SetResult(response);
            return true;
        }

        return false;
    }
}