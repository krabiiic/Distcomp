using CassandraPosts.Models;
using Cassandra.Mapping;

namespace CassandraPosts.Repositories;

public interface IPostRepository
{
    Task<Post> CreateAsync(Post post);
    Task<Post?> GetByIdAsync(Guid postId);
    Task<IEnumerable<Post>> GetAllAsync();
    Task<Post> UpdateAsync(Post post);
    Task<bool> DeleteAsync(Guid postId);
}