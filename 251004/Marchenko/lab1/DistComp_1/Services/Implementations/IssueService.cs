using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;

namespace DistComp_1.Services.Implementations;

public class IssueService : IIssueService
{
    private readonly IIssueRepository _issueRepository;
    private readonly IMapper _mapper;
    private readonly IssueRequestDTOValidator _validator;

    public IssueService(IIssueRepository issueRepository,
        IMapper mapper, IssueRequestDTOValidator validator)
    {
        _issueRepository = issueRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<IssueResponseDTO>> GetIssuesAsync()
    {
        var issues = await _issueRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<IssueResponseDTO>>(issues);
    }

    public async Task<IssueResponseDTO> GetIssueByIdAsync(long id)
    {
        var issue = await _issueRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(id));
        return _mapper.Map<IssueResponseDTO>(issue);
    }

    public async Task<IssueResponseDTO> CreateIssueAsync(IssueRequestDTO issue)
    {
        await _validator.ValidateAndThrowAsync(issue);
        var issueToCreate = _mapper.Map<Issue>(issue);
        
        issueToCreate.Created = DateTime.Now;
        issueToCreate.Modified = DateTime.Now;
        
        var createdIssue = await _issueRepository.CreateAsync(issueToCreate);
        return _mapper.Map<IssueResponseDTO>(createdIssue);
    }

    public async Task<IssueResponseDTO> UpdateIssueAsync(IssueRequestDTO issue)
    {
        await _validator.ValidateAndThrowAsync(issue);
        var issueToUpdate = _mapper.Map<Issue>(issue);
        
        issueToUpdate.Modified = DateTime.Now;
        
        var updatedIssue = await _issueRepository.UpdateAsync(issueToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(issue.Id));
        return _mapper.Map<IssueResponseDTO>(updatedIssue);
    }

    public async Task DeleteIssueAsync(long id)
    {
        if (await _issueRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(id));
        }
    }
}