using Core;
using System;
using System.Collections.Concurrent;
using System.Threading.Channels;

namespace API.Kafka
{
    public class NoteResponseListener
    {
        private readonly ConcurrentDictionary<long, Channel<KafkaMessage>> _channels = new();

        public void HandleResponse(KafkaMessage message)
        {
            if (_channels.TryRemove(message.RequestId, out var channel))
            {
                channel.Writer.TryWrite(message);
            }
        }

        public async Task<KafkaMessage?> WaitForNoteResponseAsync(long requestId, TimeSpan timeout)
        {
            var channel = Channel.CreateBounded<KafkaMessage>(1);
            _channels[requestId] = channel;

            var cts = new CancellationTokenSource(timeout);

            try
            {
                if (await channel.Reader.WaitToReadAsync(cts.Token) &&
                    channel.Reader.TryRead(out var message))
                {
                    Console.WriteLine($" -/-/-/-/-/- NoteResponseListener id = {message.RequestId} at {DateTime.Now:HH:mm:ss.fff}");
                    return message;
                }
            }
            catch (OperationCanceledException) { }

            _channels.TryRemove(requestId, out _);
            return null;
        }
    }
}
