using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using WebApplication1.DTO;

namespace WebApplication1.Service
{
    public class RemoteNoteService : IRemoteNoteService
    {
        private readonly KafkaNoteProducerService _producerService;
        private readonly HttpClient _httpClient;
        private readonly IRedisCacheService _redisCacheService;

        public RemoteNoteService(
            KafkaNoteProducerService producerService,
            HttpClient httpClient,
            IRedisCacheService redisCacheService)
        {
            _producerService = producerService;
            _httpClient = httpClient;
            _redisCacheService = redisCacheService;
        }
        public async Task<NoteResponseTo> CreateNoteAsync(NoteRequestTo dto)
        {
            if (!dto.Id.HasValue)
                dto.Id = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();

            var response = await _httpClient.PostAsJsonAsync("notes", dto);
            response.EnsureSuccessStatusCode();

            var created = await response.Content.ReadFromJsonAsync<NoteResponseTo>();

            var cacheKey = $"note_{created.Id}";
            await _redisCacheService.SetAsync(cacheKey, created);

            await _producerService.SendNoteAsync(dto);

            return created;
        }
        public async Task<NoteResponseTo> UpdateNoteAsync(NoteRequestTo dto)
        {
            var response = await _httpClient.PutAsJsonAsync($"notes/{dto.Id}", dto);
            response.EnsureSuccessStatusCode();

            var updated = await response.Content.ReadFromJsonAsync<NoteResponseTo>();

            var cacheKey = $"note_{updated.Id}";
            await _redisCacheService.SetAsync(cacheKey, updated);

            await _producerService.SendNoteAsync(dto);

            return updated;
        }

        public async Task DeleteNoteAsync(string id)
        {
            var response = await _httpClient.DeleteAsync($"notes/{id}");
            response.EnsureSuccessStatusCode();

            var cacheKey = $"note_{id}";
            await _redisCacheService.RemoveAsync(cacheKey);
        }

        public async Task<List<NoteResponseTo>> GetAllNotesAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<NoteResponseTo>>("notes");
        }

        public async Task<NoteResponseTo> GetNoteByIdAsync(long id)
        {
            var cacheKey = $"note_{id}";

            var cached = await _redisCacheService.GetAsync<NoteResponseTo>(cacheKey);
            if (cached != null)
                return cached; 

            var response = await _httpClient.GetAsync($"notes/{id}");
            if (response.StatusCode == HttpStatusCode.NotFound)
                return null;

            response.EnsureSuccessStatusCode();

            var note = await response.Content.ReadFromJsonAsync<NoteResponseTo>();
            if (note != null)
            {
                await _redisCacheService.SetAsync(cacheKey, note);
            }
            return note;
        }

    }
}
