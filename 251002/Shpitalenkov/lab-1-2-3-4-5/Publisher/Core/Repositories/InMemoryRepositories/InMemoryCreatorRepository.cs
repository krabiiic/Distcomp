using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;

namespace Core.Repositories.InMemoryRepositories;

public class InMemoryCreatorRepository : ICreatorRepository
{
    private readonly Dictionary<long, Creator> _creators = new () {};
    private long _nextId = 1;

    public InMemoryCreatorRepository()
    {
        AddCreator(new Creator()
        {
            Login = "kmjndfg@gmail.com",
            Firstname = "Денис",
            Lastname = "Шпиталенков"
        });
    }
    public async Task<Creator?> AddCreator(Creator creator)
    {
        if (_creators.Values.All(obj => obj.Login != creator.Login))
        {
            creator.Id = _nextId;
            _creators.Add(_nextId,creator);
            return _creators[_nextId++];
        }
        else
        {
            throw new BadRequestException("Such login already exists", new Dictionary<string, string[]>());
        }
    }

    public async Task<Creator?> GetCreator(long creatorId)
    {
        if (_creators.TryGetValue(creatorId, out Creator creator))
        {
            return creator;
        }

        throw new NotFoundException("Id",$"{creatorId}");
    }
    public async Task<Creator?> RemoveCreator(long creatorId)
    {
        if (_creators.Remove(creatorId, out Creator creator))
        {
            return creator;
        }
        throw new NotFoundException("Id",$"{creatorId}");
    }

    public async Task<Creator?> UpdateCreator(Creator creator)
    {
        if (_creators.ContainsKey(creator.Id))
        {
            if (_creators.Values.All(obj => obj.Login != creator.Login))
            {
                _creators[creator.Id] = creator;
                return _creators[creator.Id];
            }
            else
            {
                throw new BadRequestException("Such login already exists", new Dictionary<string, string[]>());
            }
        }
        throw new NotFoundException("Id",$"{creator.Id}");
    }
    
    public async Task<IEnumerable<Creator?>?> GetAllCreators()
    {
        return _creators.Values.ToList();
    }
}