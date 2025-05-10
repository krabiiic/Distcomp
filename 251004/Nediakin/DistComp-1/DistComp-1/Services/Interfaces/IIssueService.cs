using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IIssueService
{
    Task<IEnumerable<IssueResponseDTO>> GetIssuesAsync();

    Task<IssueResponseDTO> GetIssueByIdAsync(long id);

    Task<IssueResponseDTO> CreateIssueAsync(IssueRequestDTO issue);

    Task<IssueResponseDTO> UpdateIssueAsync(IssueRequestDTO issue);

    Task DeleteIssueAsync(long id);
}