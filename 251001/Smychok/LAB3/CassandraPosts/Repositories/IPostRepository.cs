using CassandraPosts.Models;
using Cassandra.Mapping;

namespace CassandraPosts.Repositories;

public interface IPostRepository
{
    Task<Post> CreateAsync(Post post);
    Task<Post?> GetAsync(string country, long issueId, long id);
    Task<Post?> GetByIdAsync(Guid postId);
    Task<IPage<Post>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null);
    Task<Post> UpdateAsync(Post post);
    Task<bool> DeleteAsync(string country, long issueId, long id);
    Task<bool> DeleteAsync(Guid postId);
}