using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace MyApp.Models
{
    public enum NoteState
    {
        PENDING,
        APPROVE,
        DECLINE
    }

    [JsonObject("note")]
    public class Note : BaseEntity
    {
        [Required]
        [JsonProperty("storyId")]
        public int storyId { get; set; }

        [Required]
        [MaxLength(2048)]
        [MinLength(2, ErrorMessage = "Содержание должно содержать не менее 2 символов.")]
        [JsonProperty("content")]
        public string Content { get; set; }

        [Required]
        [JsonProperty("state")]
        public NoteState State { get; set; } = NoteState.PENDING;
    }

    public class NoteRequestTo
    {
        [Required]
        [MaxLength(2048)]
        [MinLength(2, ErrorMessage = "Содержание должно содержать не менее 2 символов.")]
        public string Content { get; set; }

        [Required]
        public int StoryId { get; set; }
    }

    [JsonObject("note")]
    public class NoteResponseTo
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("content")]
        [Required]
        [MaxLength(2048)]
        [MinLength(2, ErrorMessage = "Содержание должно содержать не менее 2 символов.")]
        public string Content { get; set; }

        [JsonProperty("storyId")]
        [Required]
        public int StoryId { get; set; }

        [JsonProperty("state")]
        [Required]
        public NoteState State { get; set; }
    }
}