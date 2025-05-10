using publisher.DTO.Request;
using publisher.DTO.Response;

namespace publisher.Services.Interfaces
{
    public interface ITagService
    {
        Task<TagResponseTo> GetTagById(long id);
        Task<IEnumerable<TagResponseTo>> GetTags();
        Task<TagResponseTo> CreateTag(CreateTagRequestTo createTagRequestTo);
        Task DeleteTag(long id);
        Task<TagResponseTo> UpdateTag(UpdateTagRequestTo modifiedTag);
    }
}
