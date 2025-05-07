using DistComp_1.Exceptions;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;

namespace DistComp_1.Repositories.Implementations;

public class InMemoryCreatorRepository : BaseInMemoryRepository<Creator>, ICreatorRepository
{
    public InMemoryCreatorRepository()
    {
        _entities.Add(0, new Creator()
        {
            Login = "quantum32043@gmail.com",
            Password = "21050312",
            Firstname = "Александр",
            Lastname = "Марченко"
        });
    }
    /*
    // Индекс для поиска по логину
    private readonly Dictionary<string, long> _loginIndex = [];

    public override async Task<Creator> CreateAsync(Creator entity)
    {
        if (_loginIndex.ContainsKey(entity.Login))
        {
            throw new ConflictException(ErrorCodes.CreatorAlreadyExists, ErrorMessages.CreatorAlreadyExists(entity.Login));
        }

        var Creator = await base.CreateAsync(entity);
        _loginIndex.Add(Creator.Login, Creator.Id);

        return Creator;
    }

    public override async Task<Creator?> UpdateAsync(Creator entity)
    {
        if (_loginIndex.TryGetValue(entity.Login, out long value) && value != entity.Id)
        {
            throw new ConflictException(ErrorCodes.CreatorAlreadyExists, ErrorMessages.CreatorAlreadyExists(entity.Login));
        }

        var updatedCreator = await base.UpdateAsync(entity);
        if (updatedCreator != null)
        {
            if (_loginIndex.ContainsKey(entity.Login) && _loginIndex[entity.Login] == entity.Id)
            {
                return updatedCreator;
            }

            _loginIndex.Remove(entity.Login);
            _loginIndex.Add(updatedCreator.Login, updatedCreator.Id);
        }

        return updatedCreator;
    }

    public override async Task<Creator?> DeleteAsync(long id)
    {
        var Creator = await base.DeleteAsync(id);

        if (Creator != null)
        {
            _loginIndex.Remove(Creator.Login);
        }

        return Creator;
    }
    */
}