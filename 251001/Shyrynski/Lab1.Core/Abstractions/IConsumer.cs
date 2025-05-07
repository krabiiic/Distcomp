using Lab1.Core.Contracts;

namespace Lab1.Core.Abstractions
{
    public interface IConsumer
    {
        Task<MessageResponse?> WaitForResponseAsync(string messageId, TimeSpan timeout);
        Task StartAsync(CancellationToken cancellationToken);
        Task StopAsync(CancellationToken cancellationToken);
    }
}
