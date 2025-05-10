using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseCreatorRepository : BaseDatabaseRepository<Creator>, ICreatorRepository
{
    public DatabaseCreatorRepository(AppDbContext context) : base(context)
    {
        
    }
}