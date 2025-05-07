using System;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace WebApplication1.DTO
{
    public enum NoteState
    {
        PENDING,
        APPROVE,  
        DECLINE    
    }
    public class NoteRequestTo
    {
        public long? Id { get; set; }
        public string Country { get; set; } = "ru";
        public long ArticleId { get; set; }
        public string Content { get; set; } = string.Empty;
    }

    public class NoteResponseTo
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }
        public string Country { get; set; } = "ru";
        public long ArticleId { get; set; }
        public string Content { get; set; } = string.Empty;
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public NoteState State { get; set; } = NoteState.PENDING;
    }

}
