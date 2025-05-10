using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Response;
using Publisher.Models;
using Riok.Mapperly.Abstractions;
namespace Publisher.Mappers;

[Mapper]
public static partial class CreatorMapper
{
    public static partial Creator ToEntity(this CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo ToResponse(this Creator creator);
    public static partial IEnumerable<CreatorResponseTo> ToResponse(this IEnumerable<Creator> creators);
}