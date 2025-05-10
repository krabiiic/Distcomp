using Cassandra;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using ISession = Cassandra.ISession;

namespace Discussion.Repositories.Implementations;

public class CassandraPostRepository : IPostRepository
{
    private readonly ISession _session;
    
    public CassandraPostRepository(ISession session)
    {
        _session = session;
    }
    
    private Post MapNoteFromRow(Row row)
    {
        return new Post
        {
            Id = row.GetValue<long>("id"),
            ArticleId = row.GetValue<long>("article_id"),
            Content = row.GetValue<string>("content"),
        };
    }
    
    public async Task<IEnumerable<Post>> GetAllAsync()
    {
        var query = "SELECT * FROM tbl_post";
        var result = await _session.ExecuteAsync(new SimpleStatement(query));
        return result.Select(MapNoteFromRow).ToList();
    }

    public async Task<Post?> GetByIdAsync(long id)
    {
        var query = "SELECT * FROM tbl_post WHERE id = ?";
        var result = await _session.ExecuteAsync(new SimpleStatement(query, id));
        var row = result.FirstOrDefault();
        return row != null ? MapNoteFromRow(row) : null;
    }

    public async Task<Post> CreateAsync(Post entity)
    {
        var query = "INSERT INTO tbl_post (id, article_id, content) VALUES (?, ?, ?)";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.Id, entity.ArticleId, entity.Content);
        var a = await _session.ExecuteAsync(boundStatement);
        foreach (var item in a.GetRows())
        {
            Console.WriteLine(item);
        }
        return entity;
    }

    public async Task<Post?> UpdateAsync(Post entity)
    {
        var existingNote = await GetByIdAsync(entity.Id);
        if (existingNote == null)
            return null;

        var query = "UPDATE tbl_post SET article_id = ?, content = ? WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.ArticleId, entity.Content, entity.Id);
        await _session.ExecuteAsync(boundStatement);
        return entity;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var existingNote = await GetByIdAsync(id);
        if (existingNote == null)
            return false;

        var query = $"DELETE FROM tbl_post WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(id);
        await _session.ExecuteAsync(boundStatement);
        return true;
    }
}