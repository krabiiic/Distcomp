using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface IIssueService
{
    Task<IEnumerable<IssueResponseDTO>> GetIssuesAsync();

    Task<IssueResponseDTO> GetIssueByIdAsync(long id);

    Task<IssueResponseDTO> CreateIssueAsync(IssueRequestDTO issue);

    Task<IssueResponseDTO> UpdateIssueAsync(IssueRequestDTO issue);

    Task DeleteIssueAsync(long id);
}