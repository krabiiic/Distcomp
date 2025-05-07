
namespace Lab1.Application.Contracts
{
    public record IssueUpdateRequest(ulong Id, ulong CreatorId, string Title, string Content);
}
