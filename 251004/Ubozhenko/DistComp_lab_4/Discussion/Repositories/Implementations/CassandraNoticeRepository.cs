using Cassandra;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using ISession = Cassandra.ISession;

namespace Discussion.Repositories.Implementations;

public class CassandraReactionRepository : IReactionRepository
{
    private readonly ISession _session;
    
    public CassandraReactionRepository(ISession session)
    {
        _session = session;
    }
    
    private Reaction MapNoteFromRow(Row row)
    {
        return new Reaction
        {
            Id = row.GetValue<long>("id"),
            NewsId = row.GetValue<long>("news_id"),
            Content = row.GetValue<string>("content"),
        };
    }
    
    public async Task<IEnumerable<Reaction>> GetAllAsync()
    {
        var query = "SELECT * FROM tbl_reaction";
        var result = await _session.ExecuteAsync(new SimpleStatement(query));
        return result.Select(MapNoteFromRow).ToList();
    }

    public async Task<Reaction?> GetByIdAsync(long id)
    {
        var query = "SELECT * FROM tbl_reaction WHERE id = ?";
        var result = await _session.ExecuteAsync(new SimpleStatement(query, id));
        var row = result.FirstOrDefault();
        return row != null ? MapNoteFromRow(row) : null;
    }

    public async Task<Reaction> CreateAsync(Reaction entity)
    {
        var query = "INSERT INTO tbl_reaction (id, news_id, content) VALUES (?, ?, ?)";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.Id, entity.NewsId, entity.Content);
        var a = await _session.ExecuteAsync(boundStatement);
        foreach (var item in a.GetRows())
        {
            Console.WriteLine(item);
        }
        return entity;
    }

    public async Task<Reaction?> UpdateAsync(Reaction entity)
    {
        var existingNote = await GetByIdAsync(entity.Id);
        if (existingNote == null)
            return null;

        var query = "UPDATE tbl_reaction SET news_id = ?, content = ? WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.NewsId, entity.Content, entity.Id);
        await _session.ExecuteAsync(boundStatement);
        return entity;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var existingNote = await GetByIdAsync(id);
        if (existingNote == null)
            return false;

        var query = $"DELETE FROM tbl_reaction WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(id);
        await _session.ExecuteAsync(boundStatement);
        return true;
    }
}