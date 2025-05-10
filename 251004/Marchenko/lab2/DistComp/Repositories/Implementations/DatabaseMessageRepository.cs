using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseMessageRepository : BaseDatabaseRepository<Message>, IMessageRepository
{
    public DatabaseMessageRepository(AppDbContext context) : base(context)
    {
    }
}