using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace MyApp.Models
{
    public class Story : BaseEntity
    {
        [Required]
        public int EditorId { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Заголовок должен содержать не менее 2 символов.")]
        public string Title { get; set; }

        [Required]
        [MaxLength(2048)]
        [MinLength(4, ErrorMessage = "Содержание должно содержать не менее 4 символов.")]
        public string Content { get; set; }

        [Required]
        public DateTime Created { get; set; }

        [Required]
        public DateTime Modified { get; set; }
    }

    public class StoryRequestTo
    {
        [Required]
        public int EditorId { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Заголовок должен содержать не менее 2 символов.")]
        public string Title { get; set; }

        [Required]
        [MaxLength(2048)]
        [MinLength(4, ErrorMessage = "Содержание должно содержать не менее 4 символов.")]
        public string Content { get; set; }

        [Required]
        public DateTime Created { get; set; }

        [Required]
        public DateTime Modified { get; set; }
    }

    [JsonObject("story")]
    public class StoryResponseTo
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("title")]
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Заголовок должен содержать не менее 2 символов.")]
        public string Title { get; set; }

        [JsonProperty("content")]
        [Required]
        [MaxLength(2048)]
        [MinLength(4, ErrorMessage = "Содержание должно содержать не менее 4 символов.")]
        public string Content { get; set; }

        [JsonProperty("editorId")]
        [Required]
        public int EditorId { get; set; }

        [JsonProperty("created")]
        [Required]
        public DateTime created { get; set; }

        [JsonProperty("modified")]
        [Required]
        public DateTime modified { get; set; }
    }
}
