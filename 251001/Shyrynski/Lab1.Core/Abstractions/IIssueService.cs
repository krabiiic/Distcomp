using Lab1.Core.Contracts;
using Lab1.Core.Models;

namespace Lab1.Core.Abstractions
{
    public interface IIssueService
    {
        IssueResponseTo? GetIssue(ulong id);
        IssueResponseTo CreateIssue(Issue issue);
        bool DeleteIssue(ulong id);
        List<IssueResponseTo> GetAllIssues();
        IssueResponseTo? UpdateIssue(Issue issue, ulong id);
    }
}
