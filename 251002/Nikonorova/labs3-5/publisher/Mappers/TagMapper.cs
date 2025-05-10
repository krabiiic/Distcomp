using publisher.DTO.Request;
using publisher.DTO.Response;
using Riok.Mapperly.Abstractions;
using publisher.Models;

namespace publisher.Mappers
{
    [Mapper]
    public static partial class TagMapper
    {
        public static partial Tag ToEntity(this UpdateTagRequestTo updateTagRequestTo);
        public static partial Tag ToEntity(this CreateTagRequestTo createTagRequestTo);
        public static partial TagResponseTo ToResponse(this Tag tag);
        public static partial IEnumerable<TagResponseTo> ToResponse(this IEnumerable<Tag> tags);
    }
}
