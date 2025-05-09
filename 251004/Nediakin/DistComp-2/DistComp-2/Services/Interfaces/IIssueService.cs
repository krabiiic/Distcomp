using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IIssueService
{
    Task<IEnumerable<IssueResponseDTO>> GetIssuesAsync();

    Task<IssueResponseDTO> GetIssueByIdAsync(long id);

    Task<IssueResponseDTO> CreateIssueAsync(IssueRequestDTO issue);

    Task<IssueResponseDTO> UpdateIssueAsync(IssueRequestDTO issue);

    Task DeleteIssueAsync(long id);
}