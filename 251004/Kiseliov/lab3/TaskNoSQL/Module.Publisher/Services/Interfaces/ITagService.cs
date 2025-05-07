using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
namespace Publisher.Services.Interfaces;

public interface ITagService
{
    Task<TagResponseTo> GetTagById(long id);
    Task<IEnumerable<TagResponseTo>> GetTags();
    Task<TagResponseTo> CreateTag(CreateTagRequestTo createTagRequestTo);
    Task DeleteTag(long id);
    Task<TagResponseTo> UpdateTag(UpdateTagRequestTo modifiedTag);
}