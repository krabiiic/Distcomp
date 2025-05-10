using System.Runtime.InteropServices.JavaScript;
using Dapper;
using Domain.Entities;
using Domain.Exceptions;
using Domain.Repository;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Infrastructure.Repositories.PostgersRepositories;

public class PgNewsRepository : PgRepository,INewsRepository
{
    public PgNewsRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {
    }
    public async Task<News?> AddNews(News news)
    {
        const string sqlCreateNews = 
            """
               INSERT 
                 INTO tbl_news
                      (
                        editor_id
                      , title
                      , content
                      , created
                      , modified
                      )
               VALUES (
                        @UserId
                      , @Title
                      , @Content
                      , @Created
                      , @Modified
                      )
            RETURNING id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlCreateNews, connection);

        var time = DateTime.Now;

        cmd.Parameters.AddWithValue("UserId", news.EditorId);
        cmd.Parameters.AddWithValue("Title", news.Title);
        cmd.Parameters.AddWithValue("Content", news.Content);
        cmd.Parameters.AddWithValue("Created", time);
        cmd.Parameters.AddWithValue("Modified", time);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync()){
                var newsId = reader.GetInt64(0);
                return new News(){
                    Id = newsId,
                    EditorId = news.EditorId,
                    Title = news.Title,
                    Content = news.Content,
                    Created = time,
                    Modified = time
                };
            }
        }
        catch(NpgsqlException ex) when (ex.SqlState == "23505"){
            throw new AlreadyExistsException();
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23503"){
            throw new BadRequestException("User news not found", new Dictionary<string, string[]>());
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<News?> GetNews(long newsId)
    {
        const string sqlGetNewsById =
            """
            SELECT id
                 , editor_id
                 , title
                 , content
                 , created
                 , modified
              FROM tbl_news
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetNewsById, connection);
        cmd.Parameters.AddWithValue("Id", newsId);
        
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync()){
                var Id = reader.GetInt64(reader.GetOrdinal("id"));
                var editorId = reader.GetInt64(reader.GetOrdinal("editor_id"));
                var title = reader.GetString(reader.GetOrdinal("title"));
                var content = reader.GetString(reader.GetOrdinal("content"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                var modified = reader.GetDateTime(reader.GetOrdinal("modified"));

                return new News{
                    Id = Id,
                    EditorId = editorId,
                    Title = title,
                    Content = content,
                    Created = created,
                    Modified = modified
                };
            }
            else
            {
                new NotFoundException("Id", $"{newsId}");
            }
            throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<News?> RemoveNews(long newsId)
    {
        const string sqlDeleteNews = 
            """
            DELETE
              FROM tbl_news
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var command = new NpgsqlCommand(sqlDeleteNews, connection);
        command.Parameters.AddWithValue("Id", newsId);

        int result;

        try{
            result = await command.ExecuteNonQueryAsync();
        }
        catch(NpgsqlException ex){
            throw new BadRequestException("Error", new Dictionary<string, string[]>());
        }

        return result == 0 ? throw new NotFoundException("Id", $"{newsId}") : new News();
    }

    public async Task<News?> UpdateNews(News news)
    {
        const string sqlUpdateNews = 
            """
               UPDATE tbl_news
                  SET editor_id = @EditorId
                    , title = @Title
                    , content = @Content
                    , modified = @Modified
                WHERE id = @Id
            RETURNING created, id
            """;
        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlUpdateNews, connection);

        var time = DateTime.Now;

        cmd.Parameters.AddWithValue("Id", news.Id);
        cmd.Parameters.AddWithValue("EditorId", news.EditorId);
        cmd.Parameters.AddWithValue("Title", news.Title);
        cmd.Parameters.AddWithValue("Content", news.Content);
        cmd.Parameters.AddWithValue("Modified", time);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync()){
                var newsId = reader.GetInt64(reader.GetOrdinal("id"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                return new News(){
                    Id = newsId,
                    EditorId = news.EditorId,
                    Title = news.Title,
                    Content = news.Content,
                    Created = created,
                    Modified = time
                };
            }
        }
        catch(NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23503")
        {
            new NotFoundException("Id", $"{news.Id}");
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async void AddMarksToNews(long newsId, IEnumerable<long> markIds)
    {
        const string sqlAddMarksToNews =
            """
            INSERT INTO m2m_news_mark (news_id, mark_id)
            SELECT @NewsId, unnest(@MarkIds)
            """;
        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlAddMarksToNews, connection);
        
        cmd.Parameters.AddWithValue("NewsId", newsId);
        cmd.Parameters.AddWithValue("MarkIds", markIds.ToArray()); 

        try
        {
            await cmd.ExecuteNonQueryAsync();
        }
        catch
        {
            throw new BadRequestException("Error", new Dictionary<string, string[]>());
        }

        return;
    }

    public async Task<IEnumerable<News?>?> GetAllNews()
    {
        const string sqlGetAllNews =
            """
            SELECT id
                 , editor_id
                 , title
                 , content
                 , created
                 , modified
              FROM tbl_news
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetAllNews, connection);
        
            using var reader = await cmd.ExecuteReaderAsync();

            var newss = new List<News>();

            while (await reader.ReadAsync()){
                var newsId = reader.GetInt64(reader.GetOrdinal("id"));
                var editorId = reader.GetInt64(reader.GetOrdinal("editor_id"));
                var title = reader.GetString(reader.GetOrdinal("title"));
                var content = reader.GetString(reader.GetOrdinal("content"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                var modified = reader.GetDateTime(reader.GetOrdinal("modified"));

                newss.Add(new News{
                    Id = newsId,
                    EditorId = editorId,
                    Title = title,
                    Content = content,
                    Created = created,
                    Modified = modified
                });
            }
            return newss;
    }
}