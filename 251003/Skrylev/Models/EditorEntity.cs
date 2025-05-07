using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace MyApp.Models
{
    public class Editor : BaseEntity
    {
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Логин должен содержать не менее 2 символов.")]
        public string login { get; set; }

        [Required]
        [MaxLength(128)]
        [MinLength(8, ErrorMessage = "Пароль должен содержать не менее 8 символов.")]
        public string password { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Имя должно содержать не менее 2 символов.")]
        public string firstname { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Фамилия должна содержать не менее 2 символов.")]
        public string lastname { get; set; }
    }

    public class EditorRequestTo
    {
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Логин должен содержать не менее 2 символов.")]
        public string Login { get; set; }

        [Required]
        [MaxLength(128)]
        [MinLength(8, ErrorMessage = "Пароль должен содержать не менее 8 символов.")]
        public string Password { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Имя должно содержать не менее 2 символов.")]
        public string Firstname { get; set; }

        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Фамилия должна содержать не менее 2 символов.")]
        public string Lastname { get; set; }
    }

    [JsonObject("editor")]
    public class EditorResponseTo
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("login")]
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Логин должен содержать не менее 2 символов.")]
        public string Login { get; set; }

        [JsonProperty("password")]
        [Required]
        [MaxLength(128)]
        [MinLength(8, ErrorMessage = "Пароль должен содержать не менее 8 символов.")]
        public string Password { get; set; }

        [JsonProperty("firstname")]
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Имя должно содержать не менее 2 символов.")]
        public string Firstname { get; set; }

        [JsonProperty("lastname")]
        [Required]
        [MaxLength(64)]
        [MinLength(2, ErrorMessage = "Фамилия должна содержать не менее 2 символов.")]
        public string Lastname { get; set; }
    }
}
