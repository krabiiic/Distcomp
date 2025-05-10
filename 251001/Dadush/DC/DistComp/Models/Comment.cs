using Redis.OM.Modeling;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Diagnostics.CodeAnalysis;
using System.Text.Json.Serialization;

namespace DistComp.Models {

    [Table("tbl_comment")]
    public class Comment : EntityWithId {

        [Column("topicId")]
        public long TopicId { get; set; }

        [Column("content")]
        [Length(2, 2048)]
        public string Content { get; set; } = null!;

        [JsonIgnore]
        [MaybeNull]
        public Topic Topic { get; set; } = null!;
    }

    public class CommentInDto {

        public long TopicId { get; init; }

        [Length(2, 2048)]
        public string Content { get; init; } = null!;
    }

    [Document(Prefixes = [nameof(Comment)])]
    public class CommentOutDto {

        [RedisIdField, Indexed]
        public long Id { get; init; }

        [Indexed]
        public long TopicId { get; init; }

        [Indexed]
        public string Content { get; init; } = null!;
    }
}
