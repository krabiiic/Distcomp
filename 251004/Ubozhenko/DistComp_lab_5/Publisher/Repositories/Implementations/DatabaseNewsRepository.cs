using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseNewsRepository : BaseDatabaseRepository<News>, INewsRepository
{
    public DatabaseNewsRepository(AppDbContext context) : base(context)
    {
    }
}