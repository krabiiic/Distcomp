using Microsoft.EntityFrameworkCore.Metadata.Internal;

namespace LAB1.Services;

using DTOs;
using Repositories;
using Models;

public class WriterService
{
    private readonly IRepository<Writer> _repository;

    public WriterService(IRepository<Writer> repository)
    {
        _repository = repository;
    }
    
    private async Task<bool> IsLoginUniqueAsync(string login, int? id = null)
    {
        var writers = await _repository.GetAllAsync();
        return !writers.Any(w => w.Login == login && (id == null || w.Id != id));
    }

    public async Task<IEnumerable<WriterResponseTo>> GetAllAsync()
    {
        var writers = await _repository.GetAllAsync();
        return writers.Select(w => new WriterResponseTo
        {
            Id = w.Id,
            Login = w.Login,
            Firstname = w.Firstname,
            Lastname = w.Lastname
        });
    }

    public async Task<WriterResponseTo> GetByIdAsync(int id)
    {
        var writer = await _repository.GetByIdAsync(id);
        if (writer == null) return null;

        return new WriterResponseTo
        {
            Id = writer.Id,
            Login = writer.Login,
            Firstname = writer.Firstname,
            Lastname = writer.Lastname
        };
    }

    public async Task<WriterResponseTo> CreateAsync(WriterRequestTo writerRequest)
    {
        if (writerRequest.Login.Length < 2 || writerRequest.Login.Length > 64)
        {
            throw new ArgumentException("Login must be between 2 and 64 characters.");
        }
        
        if (writerRequest.Password.Length < 8 || writerRequest.Password.Length > 128)
        {
            throw new ArgumentException("Password must be between 8 and 128 characters.");
        }
        
        if (writerRequest.Firstname.Length < 2 || writerRequest.Firstname.Length > 64)
        {
            throw new ArgumentException("Firstname must be between 2 and 64 characters.");
        }

        if (writerRequest.Lastname.Length < 2 || writerRequest.Lastname.Length > 64)
        {
            throw new ArgumentException("Lastname must be between 2 and 64 characters.");
        }
        
        if (!await IsLoginUniqueAsync(writerRequest.Login))
        {
            throw new InvalidOperationException("Login must be unique.");
        }
        
        var writer = new Writer
        {
            Login = writerRequest.Login,
            Firstname = writerRequest.Firstname,
            Lastname = writerRequest.Lastname
        };

        await _repository.CreateAsync(writer);
        return new WriterResponseTo
        {
            Id = writer.Id,
            Login = writer.Login,
            Firstname = writer.Firstname,
            Lastname = writer.Lastname
        };
    }

    public async Task<WriterResponseTo> UpdateAsync(int id, WriterRequestTo writerRequest)
    {
        if (writerRequest.Login.Length < 2 || writerRequest.Login.Length > 64)
        {
            throw new ArgumentException("Login must be between 2 and 64 characters.");
        }
        
        if (writerRequest.Firstname.Length < 2 || writerRequest.Firstname.Length > 64)
        {
            throw new ArgumentException("Firstname must be between 2 and 64 characters.");
        }

        if (writerRequest.Lastname.Length < 2 || writerRequest.Lastname.Length > 64)
        {
            throw new ArgumentException("Lastname must be between 2 and 64 characters.");
        }
        
        if (writerRequest.Password.Length < 8 || writerRequest.Password.Length > 128)
        {
            throw new ArgumentException("Password must be between 8 and 128 characters.");
        }
        
        if (!await IsLoginUniqueAsync(writerRequest.Login, id))
        {
            throw new InvalidOperationException("Login must be unique.");
        }
        
        var writer = await _repository.GetByIdAsync(id);
        if (writer == null) return null;

        writer.Login = writerRequest.Login;
        writer.Firstname = writerRequest.Firstname;
        writer.Lastname = writerRequest.Lastname;

        await _repository.UpdateAsync(writer);
        return new WriterResponseTo
        {
            Id = writer.Id,
            Login = writer.Login,
            Firstname = writer.Firstname,
            Lastname = writer.Lastname
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}