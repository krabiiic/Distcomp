using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;

namespace Core.Repositories.InMemoryRepositories;

public class InMemoryTagRepository : ITagRepository
{
    private readonly Dictionary<long, Tag> _tags = new();
    private long _nextId = 1;
    
    public async Task<Tag?> AddTag(Tag tag)
    {
        if (_tags.Values.All(obj => obj.Name!= tag.Name))
        {
            tag.Id = _nextId;
            _tags.Add(_nextId,tag);
            return _tags[_nextId++];
        }
        else
        {
            throw new BadRequestException("Such name already exists", new Dictionary<string, string[]>());
        }
    }

    public async Task<Tag?> GetTag(long tagId)
    {
        if (_tags.TryGetValue(tagId, out Tag tag))
        {
            return tag;
        }
        throw new NotFoundException("Id",$"{tagId}");
    }

    public async Task<Tag?> RemoveTag(long tagId)
    {
        if (_tags.Remove(tagId, out Tag tag))
        {
            return tag;
        }
        throw new NotFoundException("Id",$"{tagId}");
    }

    public async Task<Tag?> UpdateTag(Tag tag)
    {
        if (_tags.ContainsKey(tag.Id))
        {
            if (_tags.Values.All(obj => obj.Name != tag.Name))
            {
                _tags[tag.Id] = tag;
                return _tags[tag.Id];
            }
            else
            {
                throw new BadRequestException("Such name already exists", new Dictionary<string, string[]>());
            }
        }
        throw new NotFoundException("Id",$"{tag.Id}");
    }

    public async Task<IEnumerable<Tag?>?> GetAllTag()
    {
        return  _tags.Values.ToList();
    }

    public async Task<IEnumerable<Tag?>?> GetTagsCreateIfNotExist(IEnumerable<string> tags)
    {
        throw new NotImplementedException();
    }
}