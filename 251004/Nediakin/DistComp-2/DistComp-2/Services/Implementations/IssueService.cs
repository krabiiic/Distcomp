using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Exceptions;
using DistComp.Infrastructure.Validators;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using DistComp.Services.Interfaces;
using FluentValidation;

namespace DistComp.Services.Implementations;

public class IssueService : IIssueService
{
    private readonly IIssueRepository _issueRepository;
    private readonly IMapper _mapper;
    private readonly IssueRequestDTOValidator _validator;
    private readonly ICreatorRepository _creatorRepository;

    public IssueService(IIssueRepository issueRepository,
        IMapper mapper, IssueRequestDTOValidator validator, ICreatorRepository creatorRepository)
    {
        _issueRepository = issueRepository;
        _mapper = mapper;
        _validator = validator;
        _creatorRepository = creatorRepository;
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

        if (issueToCreate == null || await _issueRepository.HasIssue(issueToCreate.Title))
        {
            throw new ConflictException(ErrorCodes.IssueAlreadyExists, ErrorMessages.IssueAlreadyExists(issue.Title));
        }

        if (! await _creatorRepository.HasUser(issueToCreate.CreatorId))
        {
            throw new ConflictException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(issueToCreate.CreatorId));
        }
        
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