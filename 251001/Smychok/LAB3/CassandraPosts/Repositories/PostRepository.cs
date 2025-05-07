using Cassandra;
using Cassandra.Mapping;
using CassandraPosts.Models;

namespace CassandraPosts.Repositories;

public class PostRepository : IPostRepository
{
    private readonly IMapper _mapper;

    public PostRepository(ICluster cluster)
    {
        var session = cluster.Connect("distcomp");
        _mapper = new Mapper(session);
    }

    public async Task<Post> CreateAsync(Post post)
    {
        await _mapper.InsertAsync(post);
        return post;
    }

    public async Task<Post?> GetAsync(string country, long issueId, long id)
    {
        return await _mapper.FirstOrDefaultAsync<Post>(
            "WHERE country = ? AND issue_id = ? AND id = ?", 
            country, issueId, id);
    }

    public async Task<Post?> GetByIdAsync(Guid postId)
    {
        return await _mapper.FirstOrDefaultAsync<Post>(
            "WHERE post_id = ?", postId);
    }

    public async Task<IPage<Post>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null)
    {
        return await _mapper.FetchPageAsync<Post>(
            pageSize, 
            pagingState, 
            "SELECT * FROM tbl_post", 
            new object[0]);
    }

    public async Task<Post> UpdateAsync(Post post)
    {
        await _mapper.UpdateAsync(post);
        return post;
    }

    public async Task<bool> DeleteAsync(string country, long issueId, long id)
    {
        await _mapper.DeleteAsync<Post>(
            "WHERE country = ? AND issue_id = ? AND id = ?", 
            country, issueId, id);
        return true;
    }

    public async Task<bool> DeleteAsync(Guid postId)
    {
        await _mapper.DeleteAsync<Post>("WHERE post_id = ?", postId);
        return true;
    }
}