using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace MyApp.Models
{
    public class Label : BaseEntity
    {
        [Required]
        [MaxLength(32)]
        [MinLength(2, ErrorMessage = "Название должно содержать не менее 2 символов.")]
        public string Name { get; set; }
    }

    public class LabelRequestTo
    {
        [Required]
        [MaxLength(32)]
        [MinLength(2, ErrorMessage = "Название должно содержать не менее 2 символов.")]
        public string Name { get; set; }
    }

    [JsonObject("label")]
    public class LabelResponseTo
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("name")]
        [Required]
        [MaxLength(32)]
        [MinLength(2, ErrorMessage = "Название должно содержать не менее 2 символов.")]
        public string Name { get; set; }
    }
}
