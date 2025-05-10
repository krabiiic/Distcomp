using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseNewsRepository : BaseDatabaseRepository<News>, INewsRepository
{
    public DatabaseNewsRepository(AppDbContext context) : base(context)
    {
    }
}