using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseCreatorRepository : BaseDatabaseRepository<Creator>, ICreatorRepository
{
    public DatabaseCreatorRepository(AppDbContext context) : base(context)
    {
        
    }
}