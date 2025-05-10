using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Publisher.Models;
using Riok.Mapperly.Abstractions;
namespace Publisher.Mappers;

[Mapper]
public static partial class TagMapper
{
    public static partial Tag ToEntity(this UpdateTagRequestTo updateTagRequestTo);
    public static partial Tag ToEntity(this CreateTagRequestTo createTagRequestTo);
    public static partial TagResponseTo ToResponse(this Tag tag);
    public static partial IEnumerable<TagResponseTo> ToResponse(this IEnumerable<Tag> tags);
}