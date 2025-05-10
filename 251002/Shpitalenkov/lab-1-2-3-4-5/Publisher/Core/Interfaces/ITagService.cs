using Core.DTO;

namespace Core.Interfaces;

public interface ITagService
{
    public Task<TagResponseToGetById?> CreateTag(TagRequestToCreate model);
    public Task<IEnumerable<TagResponseToGetById?>?> GetTags(TagRequestToGetAll request);
    public Task<TagResponseToGetById?> GetTagById(TagRequestToGetById request);
    public Task<TagResponseToGetById?> UpdateTag(TagRequestToFullUpdate model);
    public Task<TagResponseToGetById?> DeleteTag(TagRequestToDeleteById request);
    
    
    public Task<IEnumerable<TagResponseToGetById>?> CreateTagsIfDontExist(
        IEnumerable<string> request);
}