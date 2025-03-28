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
            Login = "nedyasha@gmail.com",
            Password = "21050312",
            Firstname = "Александр",
            Lastname = "Недякин"
        });
    }
}