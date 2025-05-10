using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class IssueService : IIssueService
{
    private readonly IIssueRepository _issueRepository;
    private readonly ICreatorRepository _creatorRepository;
    private readonly IMapper _mapper;
    private readonly IssueRequestDTOValidator _validator;

    public IssueService(IIssueRepository issueRepository, ICreatorRepository creatorRepository,
        IMapper mapper, IssueRequestDTOValidator validator)
    {
        _issueRepository = issueRepository;
        _creatorRepository = creatorRepository;
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

        issueToCreate.CreatorId = issue.CreatorId;
        issueToCreate.Created = DateTime.UtcNow;
        issueToCreate.Modified = DateTime.UtcNow;
        
        var createdIssue = await _issueRepository.CreateAsync(issueToCreate);
        return _mapper.Map<IssueResponseDTO>(createdIssue);
    }

    public async Task<IssueResponseDTO> UpdateIssueAsync(IssueRequestDTO issue)
    {
        await _validator.ValidateAndThrowAsync(issue);
        var issueToUpdate = _mapper.Map<Issue>(issue);
        
        issueToUpdate.Modified = DateTime.UtcNow;
        
        var updatedIssue = await _issueRepository.UpdateAsync(issueToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(issue.Id));
        return _mapper.Map<IssueResponseDTO>(updatedIssue);
    }

    public async Task DeleteIssueAsync(long id)
    {
        if (!await _issueRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(id));
        }
    }
}