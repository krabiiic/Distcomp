using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseMarkRepository : BaseDatabaseRepository<Mark>, IMarkRepository
{
    public DatabaseMarkRepository(AppDbContext context) : base(context)
    {
    }
}