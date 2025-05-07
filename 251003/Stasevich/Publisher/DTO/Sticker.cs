using System.Text.Json.Serialization;

namespace WebApplication1.DTO
{
    public class StickerRequestTo
    {
        public long? Id { get; set; }
        public string Name { get; set; } = string.Empty;
    }

    public class StickerResponseTo
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }

        [JsonPropertyName("name")]
        public string Name { get; set; } = string.Empty;
    }
}
