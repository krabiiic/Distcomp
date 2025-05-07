using DistComp.Models;
using Redis.OM;
using System;

namespace DistComp.Services.Background {
    public class RedisIndexCreationService : IHostedService {
        private readonly RedisConnectionProvider provider;

        public RedisIndexCreationService(RedisConnectionProvider provider) {
            this.provider = provider;
        }

        public async Task StartAsync(CancellationToken cancellationToken) {
            await provider.Connection.CreateIndexAsync(typeof(UserOutDto));
            await provider.Connection.CreateIndexAsync(typeof(TopicOutDto));
            await provider.Connection.CreateIndexAsync(typeof(TagOutDto));
            await provider.Connection.CreateIndexAsync(typeof(CommentOutDto));
        }

        public Task StopAsync(CancellationToken cancellationToken) {
            return Task.CompletedTask;
        }
    }
}
