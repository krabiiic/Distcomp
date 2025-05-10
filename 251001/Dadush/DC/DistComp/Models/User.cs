using Redis.OM.Modeling;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace DistComp.Models {

    [Table("tbl_user")]
    public class User : EntityWithId {

        [Column("login")]
        [Length(2, 64)]
        public string Login { get; set; } = null!;

        [Column("password")]
        [Length(8, 128)]
        public string Password { get; set; } = null!;

        [Column("firstname")]
        [Length(2, 64)]
        public string Firstname { get; set; } = null!;

        [Column("lastname")]
        [Length(2, 64)]
        public string Lastname { get; set; } = null!;

        [JsonIgnore]
        public ICollection<Topic> Topics { get; set; } = [];
    }

    public class UserInDto {

        [Length(2, 64)]
        public string Login { get; init; } = null!;

        [Length(8, 128)]
        public string Password { get; init; } = null!;

        [Length(2, 64)]
        public string Firstname { get; init; } = null!;

        [Length(2, 64)]
        public string Lastname { get; init; } = null!;
    }

    [Document(Prefixes = [nameof(User)])]
    public class UserOutDto {

        [RedisIdField, Indexed]
        public long Id { get; init; }

        [Indexed]
        public string Login { get; init; } = null!;

        [Indexed]
        public string Firstname { get; init; } = null!;

        [Indexed]
        public string Lastname { get; init; } = null!;
    }
}
