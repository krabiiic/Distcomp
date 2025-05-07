using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using Riok.Mapperly.Abstractions;
using lab2_jpa.Models;

namespace lab2_jpa.Mappers
{
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
}
