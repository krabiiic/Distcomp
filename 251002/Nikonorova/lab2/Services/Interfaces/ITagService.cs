using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Services.Interfaces
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
