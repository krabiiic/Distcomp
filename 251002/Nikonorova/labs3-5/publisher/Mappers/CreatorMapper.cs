using Microsoft.AspNetCore.Http.HttpResults;
using publisher.DTO.Request;
using publisher.DTO.Response;
using Riok.Mapperly.Abstractions;
using publisher.Models;

namespace publisher.Mappers
{
    [Mapper]
    public static partial class CreatorMapper
    {
        public static partial Creator ToEntity(this CreateCreatorRequestTo createCreatorRequestTo);
        public static partial CreatorResponseTo ToResponse(this Creator creator);
        public static partial IEnumerable<CreatorResponseTo> ToResponse(this IEnumerable<Creator> creators);
    }
}
