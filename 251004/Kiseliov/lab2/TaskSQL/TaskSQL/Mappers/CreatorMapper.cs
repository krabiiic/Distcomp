using Riok.Mapperly.Abstractions;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;

namespace TaskSQL.Mappers;

[Mapper]
public static partial class CreatorMapper
{
    public static partial Creator ToEntity(this CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo ToResponse(this Creator creator);
    public static partial Creator Map(CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo Map(Creator creator);
    public static partial IEnumerable<CreatorResponseTo> Map(IEnumerable<Creator> creators);
}