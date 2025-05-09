using DistComp.Data;
using DistComp.Exceptions;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseIssueRepository : BaseDatabaseRepository<Issue>, IIssueRepository
{
    public DatabaseIssueRepository(AppDbContext context) : base(context)
    {
    }

    public virtual async Task<bool> HasIssue(string issue) => _dbSet.Any(i => i.Title == issue);
    public virtual async Task<bool> HasIssue(long issueId) => _dbSet.Any(i => i.Id == issueId);
}