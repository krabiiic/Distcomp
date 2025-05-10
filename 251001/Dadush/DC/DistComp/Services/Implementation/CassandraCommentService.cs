using AutoMapper;
using DistComp.Controllers;
using DistComp.Data;
using DistComp.Models;
using Microsoft.AspNetCore.Mvc;
using Redis.OM;
using Redis.OM.Searching;
using System.Text.Json;

namespace DistComp.Services.Implementation {
    public class CassandraCommentService : ICommentService, IDisposable {

        private DCContext context;
        private RedisConnectionProvider provider;
        private IRedisCollection<CommentOutDto> comments;
        private IMapper mapper;

        private HttpClient client;
        private string connectionString;
        private string apiPrefix;

        private JsonSerializerOptions options;

        public CassandraCommentService(
            DCContext context, IMapper mapper,
            RedisConnectionProvider provider, IConfiguration configuration
        ) {
            this.context = context;
            this.mapper = mapper;
            connectionString = configuration.GetConnectionString("discussion")!;

            this.provider = provider;
            comments = provider.RedisCollection<CommentOutDto>();

            client = new HttpClient();
            client.BaseAddress = new Uri(connectionString);

            // Get API perfix
            apiPrefix = "/" + "api/v1.0/comments";

            // Options
            options = new JsonSerializerOptions {
                PropertyNameCaseInsensitive = true
            };
        }

        public async Task<IEnumerable<CommentOutDto>> GetAll() {
            var cached = await comments.ToListAsync();
            if (cached.Count > 0) {
                return cached;
            }

            var response = await client.GetAsync(apiPrefix);
            string content = await response.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<IEnumerable<CommentOutDto>>(content, options)!;
        }

        public async Task<CommentOutDto?> Get(long id) {
            var cached = await comments.FindByIdAsync(id.ToString());
            if (cached is not null) {
                return cached;
            }

            var response = await client.GetAsync(apiPrefix + "/" + id);
            string content = await response.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<CommentOutDto>(content, options)!;
        }

        public async Task<CommentOutDto> Create(CommentInDto data) {
            var topic = await context.Topics.FindAsync(data.TopicId);
            if (topic is null) {
                throw new KeyNotFoundException("Topic with specified ID does not exist");
            }

            var response = await client.PostAsync(apiPrefix, JsonContent.Create(data));
            response.EnsureSuccessStatusCode();
            string content = await response.Content.ReadAsStringAsync();
            var commentOut = JsonSerializer.Deserialize<CommentOutDto>(content, options)!;

            await comments.InsertAsync(commentOut);

            return commentOut;
        }

        public async Task<CommentOutDto> Update(Comment data) {
            var topic = await context.Topics.FindAsync(data.TopicId);
            if (topic is null) {
                throw new KeyNotFoundException("Topic with specified ID does not exist");
            }

            var response = await client.PutAsync(apiPrefix, JsonContent.Create(new {
                country = "by",
                id = data.Id,
                topicId = data.TopicId,
                content = data.Content,
            }));
            response.EnsureSuccessStatusCode();
            string content = await response.Content.ReadAsStringAsync();
            var commentOut = JsonSerializer.Deserialize<CommentOutDto>(content, options)!;

            await comments.UpdateAsync(commentOut);

            return commentOut;
        }

        public async Task Delete(long id) {
            var response = await client.DeleteAsync(apiPrefix + "/" + id);
            if (response.StatusCode == System.Net.HttpStatusCode.NotFound) {
                throw new KeyNotFoundException("hihi");
            }
            response.EnsureSuccessStatusCode();

            await provider.Connection.UnlinkAsync($"{nameof(Comment)}:{id}");
        }

        public void Dispose() {
            client.Dispose();
        }
    }
}
