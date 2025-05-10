using Microsoft.EntityFrameworkCore.Metadata.Internal;

namespace LAB2.Services;

using DTOs;
using Repositories;
using Models;

public class WriterService
{
    private readonly IRepository<Writer> _repository;
    private readonly IRepository<Issue> _issueRepository;

    public WriterService(IRepository<Writer> repository, IRepository<Issue> issueRepository)
    {
        _repository = repository;
        _issueRepository = issueRepository;
    }
    
    private async Task<bool> IsLoginUniqueAsync(string login, int? id = null)
    {
        var writers = await _repository.GetAllAsync();
        return !writers.Any(w => w.login == login && (id == null || w.id != id));
    }

    public async Task<IEnumerable<WriterResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var writers = await _repository.GetAllAsync(queryParams);
        return writers.Select(w => new WriterResponseTo
        {
            Id = w.id,
            Login = w.login,
            Firstname = w.firstname,
            Lastname = w.lastname
        });
    }

    public async Task<WriterResponseTo> GetByIdAsync(int id)
    {
        var writer = await _repository.GetByIdAsync(id);
        if (writer == null) return null;

        return new WriterResponseTo
        {
            Id = writer.id,
            Login = writer.login,
            Firstname = writer.firstname,
            Lastname = writer.lastname
        };
    }

    public async Task<WriterResponseTo> CreateAsync(WriterRequestTo writerRequest)
    {
        if (string.IsNullOrEmpty(writerRequest.Login))
        {
            throw new ArgumentException("Login cannot be empty.");
        }

        if (writerRequest.Login.Length > 64)
        {
            throw new ArgumentException("Login must be less than 64 characters.");
        }
        
        if (writerRequest.Login.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }

        if (string.IsNullOrEmpty(writerRequest.Password))
        {
            throw new ArgumentException("Password cannot be empty.");
        }
        
        if (writerRequest.Password.Length < 8)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }

        if (writerRequest.Password.Length > 128)
        {
            throw new ArgumentException("Password must be less than 128 characters.");
        }

        if (string.IsNullOrEmpty(writerRequest.Firstname))
        {
            throw new ArgumentException("Firstname cannot be empty.");
        }

        if (string.IsNullOrEmpty(writerRequest.Lastname))
        {
            throw new ArgumentException("Lastname cannot be empty.");
        }
        
        if (writerRequest.Firstname.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }
        
        if (writerRequest.Lastname.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }

        if (!await IsLoginUniqueAsync(writerRequest.Login))
        {
            throw new InvalidOperationException("Login must be unique.");
        }

        var writer = new Writer
        {
            login = writerRequest.Login,
            password = writerRequest.Password,
            firstname = writerRequest.Firstname,
            lastname = writerRequest.Lastname
        };

        await _repository.CreateAsync(writer);
        return new WriterResponseTo
        {
            Id = writer.id,
            Login = writer.login,
            Firstname = writer.firstname,
            Lastname = writer.lastname
        };
    }

    public async Task<WriterResponseTo> UpdateAsync(WriterRequestTo writerRequest)
    {
        if (string.IsNullOrEmpty(writerRequest.Login))
        {
            throw new ArgumentException("Login cannot be empty.");
        }

        if (writerRequest.Login.Length > 64)
        {
            throw new ArgumentException("Login must be less than 64 characters.");
        }
        
        if (writerRequest.Login.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }

        if (string.IsNullOrEmpty(writerRequest.Password))
        {
            throw new ArgumentException("Password cannot be empty.");
        }

        if (writerRequest.Password.Length < 8)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }
        
        if (writerRequest.Password.Length > 128)
        {
            throw new ArgumentException("Password must be less than 128 characters.");
        }

        if (string.IsNullOrEmpty(writerRequest.Firstname))
        {
            throw new ArgumentException("Firstname cannot be empty.");
        }

        if (string.IsNullOrEmpty(writerRequest.Lastname))
        {
            throw new ArgumentException("Lastname cannot be empty.");
        }
        
        if (writerRequest.Firstname.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }
        
        if (writerRequest.Lastname.Length < 2)
        {
            throw new ArgumentException("Login must be more than 2 characters.");
        }

        if (!await IsLoginUniqueAsync(writerRequest.Login, writerRequest.id))
        {
            throw new InvalidOperationException("Login must be unique.");
        }

        var writer = await _repository.GetByIdAsync(writerRequest.id);
        if (writer == null) return null;

        writer.login = writerRequest.Login;
        writer.firstname = writerRequest.Firstname;
        writer.lastname = writerRequest.Lastname;

        await _repository.UpdateAsync(writer);
        return new WriterResponseTo
        {
            Id = writer.id,
            Login = writer.login,
            Firstname = writer.firstname,
            Lastname = writer.lastname
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var writer = await _repository.GetByIdAsync(id);
        if (writer == null) return false;

        // Удаляем все Issue, связанные с Writer
        foreach (var issue in writer.issues.ToList())
        {
            // Удаляем записи из промежуточной таблицы IssueSticker
            foreach (var sticker in issue.stickers.ToList())
            {
                issue.stickers.Remove(sticker);
            }

            // Удаляем Issue
            await _issueRepository.DeleteAsync(issue.id);
        }
        
        return await _repository.DeleteAsync(id);
    }
}