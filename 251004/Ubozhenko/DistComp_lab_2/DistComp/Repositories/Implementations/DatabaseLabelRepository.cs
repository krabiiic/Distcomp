using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseLabelRepository : BaseDatabaseRepository<Label>, ILabelRepository
{
    public DatabaseLabelRepository(AppDbContext context) : base(context)
    {
    }
}