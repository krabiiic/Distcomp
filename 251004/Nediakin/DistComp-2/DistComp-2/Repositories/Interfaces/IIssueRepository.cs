using DistComp.Models;

namespace DistComp.Repositories.Interfaces;

public interface IIssueRepository : IRepository<Issue>
{
    public Task<bool> HasIssue(string issue);
    public Task<bool> HasIssue(long issueId);
}