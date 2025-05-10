using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseAuthorRepository : BaseDatabaseRepository<Author>, IAuthorRepository
{
    public DatabaseAuthorRepository(AppDbContext context) : base(context)
    {
        
    }
}