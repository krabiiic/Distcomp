using Core;
using StackExchange.Redis;
using System.Text.Json;

namespace API.Redis
{
    public class NoteCacheService
    {
        private readonly IDatabase _cache;
        private const string NotePrefix = "note:";

        public NoteCacheService(IConnectionMultiplexer redis)
        {
            _cache = redis.GetDatabase();
        }

        public async Task<Note?> GetNoteAsync(long id)
        {
            var value = await _cache.StringGetAsync(NotePrefix + id);
            if (value.IsNullOrEmpty) return null;
            return JsonSerializer.Deserialize<Note>(value!);
        }

        public async Task SetNoteAsync(Note note)
        {
            var value = JsonSerializer.Serialize(note);
            await _cache.StringSetAsync(NotePrefix + note.Id, value, TimeSpan.FromMinutes(10));
        }

        public async Task RemoveNoteAsync(long id)
        {
            await _cache.KeyDeleteAsync(NotePrefix + id);
        }
    }
}
