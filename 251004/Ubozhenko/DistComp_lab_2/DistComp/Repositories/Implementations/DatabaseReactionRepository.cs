using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseReactionRepository : BaseDatabaseRepository<Reaction>, IReactionRepository
{
    public DatabaseReactionRepository(AppDbContext context) : base(context)
    {
    }
}