using Riok.Mapperly.Abstractions;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;

namespace TaskSQL.Mappers;

[Mapper]
public static partial class TagMapper
{
    public static partial Tag Map(UpdateTagRequestTo updateTagRequestTo);
    public static partial Tag Map(CreateTagRequestTo createTagRequestTo);
    public static partial TagResponseTo Map(Tag tag);
    public static partial IEnumerable<TagResponseTo> Map(IEnumerable<Tag> tags);

    public static partial IEnumerable<Tag> Map(
        IEnumerable<UpdateTagRequestTo> tagRequestTos);
}