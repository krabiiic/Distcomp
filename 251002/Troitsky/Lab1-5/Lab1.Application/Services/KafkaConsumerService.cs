using Microsoft.Extensions.Hosting;
using Lab1.Core.Abstractions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab1.Application.Services
{
    // Для автоматического подключения к Kafka через стандартный механизм
    public class KafkaConsumerService : IHostedService
    {
        private Task? _executingTask;
        private CancellationTokenSource? _cts;
        private readonly IConsumer _consumer;

        public KafkaConsumerService(IConsumer consumer)
        {
            _consumer = consumer;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _cts = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            _executingTask = Task.Run(() => _consumer.StartAsync(_cts.Token), _cts.Token);
            // Синхронно запущена фоновая задача
            return Task.CompletedTask;
        }

        public async Task StopAsync(CancellationToken cancellationToken)
        {
            if (_cts != null)
            {
                _cts.Cancel();
                _cts.Dispose();
            }

            if (_executingTask != null)
            {
                await _executingTask;
            }


            await _consumer.StopAsync(cancellationToken);
        }
    }
}
