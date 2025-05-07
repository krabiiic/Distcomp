using Core.Entities;

namespace Core.Interfaces;

public interface ITagRepository
{
    Task<Tag?> AddTag(Tag tag);
    Task<Tag?> GetTag(long tagId);
    Task<Tag?> RemoveTag(long tagId);
    Task<Tag?> UpdateTag(Tag tag);
    Task<IEnumerable<Tag?>?> GetAllTag();
    
    Task<IEnumerable<Tag?>?> GetTagsCreateIfNotExist(IEnumerable<string> tags);
}