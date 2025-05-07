using System;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace Discussion.DTO
{
    public enum NoteState
    {
        PENDING,  
        APPROVE, 
        DECLINE  
    }
    public class NoteRequestTo
    {
        [JsonPropertyName("id")]
        public long? Id { get; set; }
        public string Country { get; set; } = "ru";
        public long ArticleId { get; set; }
        public string Content { get; set; } = string.Empty;
    }
    public class NoteResponseTo
    {
        public long Id { get; set; }
        public string Country { get; set; } = "ru";
        public long ArticleId { get; set; }
        public string Content { get; set; } = string.Empty;
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public NoteState State { get; set; } = NoteState.PENDING;
    }
    public class NumericStringConverter : JsonConverter<string>
    {
        public override string Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            if (reader.TokenType == JsonTokenType.Number)
            {
                long value = reader.GetInt64();
                return value.ToString();
            }
            else if (reader.TokenType == JsonTokenType.String)
            {
                return reader.GetString()!;
            }
            throw new JsonException("Unexpected token type");
        }

        public override void Write(Utf8JsonWriter writer, string value, JsonSerializerOptions options)
        {
            if (long.TryParse(value, out long numericValue))
            {
                writer.WriteNumberValue(numericValue);
            }
            else
            {
                writer.WriteStringValue(value);
            }
        }
    }
}
