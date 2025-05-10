using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseIssueRepository : BaseDatabaseRepository<Issue>, IIssueRepository
{
    public DatabaseIssueRepository(AppDbContext context) : base(context)
    {
    }
}