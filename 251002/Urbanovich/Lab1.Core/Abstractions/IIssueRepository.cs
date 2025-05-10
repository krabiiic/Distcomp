using Lab1.Core.Contracts;
using Lab1.Core.Models;
namespace Lab1.Core.Abstractions
{
    public interface IIssueRepository
    {
        IssueResponseTo? Get(ulong id);
        IssueResponseTo Create(Issue issue);
        List<IssueResponseTo> GetAll();
        bool Delete(ulong id);
        IssueResponseTo? Update(Issue issue, ulong id);
    }
}
