using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using Microsoft.AspNetCore.Http.HttpResults;
using Riok.Mapperly.Abstractions;
using lab2_jpa.Models;

namespace lab2_jpa.Mappers
{
    [Mapper]
    public static partial class CreatorMapper
    {
        public static partial Creator ToEntity(this CreateCreatorRequestTo createCreatorRequestTo);
        public static partial CreatorResponseTo ToResponse(this Creator creator);
        public static partial Creator Map(CreateCreatorRequestTo createCreatorRequestTo);
        public static partial CreatorResponseTo Map(Creator creator);
        public static partial IEnumerable<CreatorResponseTo> Map(IEnumerable<Creator> creators);
    }
}
