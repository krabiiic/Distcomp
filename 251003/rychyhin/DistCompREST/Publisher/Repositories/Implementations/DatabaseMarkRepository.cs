using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseLabelRepository : BaseDatabaseRepository<Label>, ILabelRepository
{
    public DatabaseLabelRepository(AppDbContext context) : base(context)
    {
    }
}