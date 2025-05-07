using Core.Entities;

namespace Core.Interfaces;

public interface ICreatorRepository
{
    Task<Creator?> AddCreator(Creator creator);
    Task<Creator?> GetCreator(long creatorId);
    Task<Creator?> RemoveCreator(long creatorId);
    Task<Creator?> UpdateCreator(Creator creator);

    Task<IEnumerable<Creator?>?> GetAllCreators();
}