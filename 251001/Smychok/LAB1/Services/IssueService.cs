using Microsoft.EntityFrameworkCore.Metadata.Internal;

namespace LAB1.Services;

using DTOs;
using Repositories;
using Models;


public class IssueService
{
    private readonly IRepository<Issue> _repository;

    public IssueService(IRepository<Issue> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<IssueResponseTo>> GetAllAsync()
    {
        var issues = await _repository.GetAllAsync();
        return issues.Select(i => new IssueResponseTo
        {
            Id = i.Id,
            Title = i.Title,
            Content = i.Content,
            Created = i.Created,
            Modified = i.Modified,
            WriterId = i.WriterId
        });
    }

    public async Task<IssueResponseTo> GetByIdAsync(int id)
    {
        var issue = await _repository.GetByIdAsync(id);
        if (issue == null) return null;

        return new IssueResponseTo
        {
            Id = issue.Id,
            Title = issue.Title,
            Content = issue.Content,
            Created = issue.Created,
            Modified = issue.Modified,
            WriterId = issue.WriterId
        };
    }

    public async Task<IssueResponseTo> CreateAsync(IssueRequestTo issueRequest)
    {
        if (issueRequest.Title.Length < 2 || issueRequest.Title.Length > 64)
        {
            throw new ArgumentException("Title must be between 2 and 64 characters.");
        }
        
        var issue = new Issue
        {
            Title = issueRequest.Title,
            Content = issueRequest.Content,
            WriterId = issueRequest.WriterId,
            Created = DateTime.UtcNow,
            Modified = DateTime.UtcNow
        };

        await _repository.CreateAsync(issue);
        return new IssueResponseTo
        {
            Id = issue.Id,
            Title = issue.Title,
            Content = issue.Content,
            Created = issue.Created,
            Modified = issue.Modified,
            WriterId = issue.WriterId
        };
    }

    public async Task<IssueResponseTo> UpdateAsync(int id, IssueRequestTo issueRequest)
    {
        if (issueRequest.Title.Length < 2 || issueRequest.Title.Length > 64)
        {
            throw new ArgumentException("Title must be between 2 and 64 characters.");
        }
        
        var issue = await _repository.GetByIdAsync(id);
        if (issue == null) return null;

        issue.Title = issueRequest.Title;
        issue.Content = issueRequest.Content;
        issue.WriterId = issueRequest.WriterId;
        issue.Modified = DateTime.UtcNow;

        await _repository.UpdateAsync(issue);
        return new IssueResponseTo
        {
            Id = issue.Id,
            Title = issue.Title,
            Content = issue.Content,
            Created = issue.Created,
            Modified = issue.Modified,
            WriterId = issue.WriterId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}