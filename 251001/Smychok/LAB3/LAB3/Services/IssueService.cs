using LAB2.Data;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.EntityFrameworkCore;
using System.Net.Http.Json;

namespace LAB2.Services;

using DTOs;
using Repositories;
using Models;


public class IssueService
{
    private readonly IRepository<Issue> _repository;
    private readonly IRepository<Sticker> _stickerRepository;
    private readonly AppDbContext _context; 

    public IssueService(IRepository<Issue> repository, IRepository<Sticker> stickerRepository, AppDbContext context)
    {
        _repository = repository;
        _stickerRepository = stickerRepository;
        _context = context;
    }

    private async Task<bool> IsTitleUniqueAsync(string title, int? id = null)
    {
        var issues = await _repository.GetAllAsync();
        return !issues.Any(w => w.title == title && (id == null || w.id != id));
    }
    
    public async Task<IEnumerable<IssueResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var issues = await _repository.GetAllAsync(queryParams);
        return issues.Select(i => new IssueResponseTo
        {
            Id = i.id,
            Title = i.title,
            Content = i.content,
            Created = i.created,
            Modified = i.modified,
            WriterId = i.writer_id
        });
    }

    public async Task<IssueResponseTo> GetByIdAsync(int id)
    {
        var issue = await _repository.GetByIdAsync(id);
        if (issue == null) return null;

        return new IssueResponseTo
        {
            Id = issue.id,
            Title = issue.title,
            Content = issue.content,
            Created = issue.created,
            Modified = issue.modified,
            WriterId = issue.writer_id
        };
    }

    public async Task<IssueResponseTo> CreateAsync(IssueRequestTo issueRequest)
    {
        if (string.IsNullOrEmpty(issueRequest.Title))
        {
            throw new ArgumentException("Title cannot be empty.");
        }

        if (issueRequest.Title.Length > 64)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }

        if (string.IsNullOrEmpty(issueRequest.Content))
        {
            throw new ArgumentException("Content cannot be empty.");
        }

        if (issueRequest.Title.Length < 2)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (issueRequest.Content.Length > 2048)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (issueRequest.Content.Length < 4)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (!await IsTitleUniqueAsync(issueRequest.Title))
        {
            throw new InvalidOperationException("Login must be unique.");
        }
        
        var issue = new Issue
        {
            writer_id = issueRequest.WriterId,
            title = issueRequest.Title,
            content = issueRequest.Content,
            created = DateTime.UtcNow,
            modified = DateTime.UtcNow
        };
        
        if (issueRequest.Stickers != null)
        {
            foreach (var stickerName in issueRequest.Stickers)
            {
                // Ищем стикер по имени или создаем новый
                var sticker = await _stickerRepository.GetQueryable().FirstOrDefaultAsync(s => s.name == stickerName);
                    
                if (sticker == null)
                {
                    sticker = new Sticker { name = stickerName };
                    await _stickerRepository.CreateAsync(sticker); // Сохраняем стикер в базе данных
                }
                
                
                // Добавляем связь Issue-Sticker
                issue.stickers.Add(sticker);
            }
        }
        
        await _repository.CreateAsync(issue);
        return new IssueResponseTo
        {
            Id = issue.id,
            Title = issue.title,
            Content = issue.content,
            Created = issue.created,
            Modified = issue.modified,
            WriterId = issue.writer_id
        };
    }

    public async Task<IssueResponseTo> UpdateAsync(IssueRequestTo issueRequest)
    {
        if (string.IsNullOrEmpty(issueRequest.Title))
        {
            throw new ArgumentException("Title cannot be empty.");
        }

        if (issueRequest.Title.Length > 64)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }

        if (string.IsNullOrEmpty(issueRequest.Content))
        {
            throw new ArgumentException("Content cannot be empty.");
        }

        if (issueRequest.Title.Length < 2)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (issueRequest.Content.Length > 2048)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (issueRequest.Content.Length < 4)
        {
            throw new ArgumentException("Title must be less than 64 characters.");
        }
        
        if (!await IsTitleUniqueAsync(issueRequest.Title))
        {
            throw new InvalidOperationException("Login must be unique.");
        }
        
        var issue = await _repository.GetByIdAsync(issueRequest.id);
        if (issue == null) return null;

        issue.title = issueRequest.Title;
        issue.content = issueRequest.Content;
        issue.writer_id = issueRequest.WriterId;
        issue.modified = DateTime.UtcNow;

        await _repository.UpdateAsync(issue);
        return new IssueResponseTo
        {
            Id = issue.id,
            Title = issue.title,
            Content = issue.content,
            Created = issue.created,
            Modified = issue.modified,
            WriterId = issue.writer_id
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var issue = await _repository.GetByIdAsync(id);
        if (issue == null) return false;

        await _context.Entry(issue)
            .Collection(i => i.stickers)
            .LoadAsync();
        
        // Удаляем записи из промежуточной таблицы IssueSticker
        foreach (var sticker in issue.stickers.ToList())
        {
            await _stickerRepository.DeleteAsync(sticker.id);
        }
        return await _repository.DeleteAsync(id);
    }
    
    private readonly HttpClient _httpClient = new()
    {
        BaseAddress = new Uri("http://localhost:24130/api/v1.0/")
    };

    public async Task<Post> CreatePostAsync(Post post)
    {
        var response = await _httpClient.PostAsJsonAsync("posts", post);
        return await response.Content.ReadFromJsonAsync<Post>();
    }
}