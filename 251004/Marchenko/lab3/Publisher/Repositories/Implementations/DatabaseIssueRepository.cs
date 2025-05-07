using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseIssueRepository : BaseDatabaseRepository<Issue>, IIssueRepository
{
    public DatabaseIssueRepository(AppDbContext context) : base(context)
    {
    }
}