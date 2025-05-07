namespace Lab3.Core.Abstractions
{
    public interface IProducer
    {
        Task SendMessageAsync(string key, object message);
    }
}
