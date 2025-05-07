using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;

namespace TaskSQL.Services.Interfaces;

public interface ITagService
{
    Task<TagResponseTo> GetTagById(long id);
    Task<IEnumerable<TagResponseTo>> GetTags();
    Task<TagResponseTo> CreateTag(CreateTagRequestTo createTagRequestTo);
    Task DeleteTag(long id);
    Task<TagResponseTo> UpdateTag(UpdateTagRequestTo modifiedTag);
}