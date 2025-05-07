using System.Runtime.InteropServices.JavaScript;
using Dapper;
using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Core.Repositories.PostgersRepositories;

public class PgArticleRepository : PgRepository,IArticleRepository
{
    public PgArticleRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {
    }
    public async Task<Article?> AddArticle(Article article)
    {
        const string sqlCreateArticle =
            """
               INSERT 
                 INTO tbl_article
                      (
                        creator_id
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
        await using var cmd = new NpgsqlCommand(sqlCreateArticle, connection);

        var time = DateTime.Now;

        cmd.Parameters.AddWithValue("UserId", article.CreatorId);
        cmd.Parameters.AddWithValue("Title", article.Title);
        cmd.Parameters.AddWithValue("Content", article.Content);
        cmd.Parameters.AddWithValue("Created", time);
        cmd.Parameters.AddWithValue("Modified", time);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync()){
                var articleId = reader.GetInt64(0);
                return new Article(){
                    Id = articleId,
                    CreatorId = article.CreatorId,
                    Title = article.Title,
                    Content = article.Content,
                    Created = time,
                    Modified = time
                };
            }
        }
        catch(NpgsqlException ex) when (ex.SqlState == "23505"){
            throw new AlreadyExistsException();
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23503"){
            throw new BadRequestException("User article not found", new Dictionary<string, string[]>());
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<Article?> GetArticle(long articleId)
    {
        const string sqlGetArticleById =
            """
            SELECT id
                 , creator_id
                 , title
                 , content
                 , created
                 , modified
              FROM tbl_article
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetArticleById, connection);
        cmd.Parameters.AddWithValue("Id", articleId);
        
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync()){
                var Id = reader.GetInt64(reader.GetOrdinal("id"));
                var creatord = reader.GetInt64(reader.GetOrdinal("creator_id"));
                var title = reader.GetString(reader.GetOrdinal("title"));
                var content = reader.GetString(reader.GetOrdinal("content"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                var modified = reader.GetDateTime(reader.GetOrdinal("modified"));

                return new Article{
                    Id = Id,
                    CreatorId = creatord,
                    Title = title,
                    Content = content,
                    Created = created,
                    Modified = modified
                };
            }
            else
            {
                new NotFoundException("Id", $"{articleId}");
            }
            throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<Article?> RemoveArticle(long articleId)
    {
        const string sqlDeleteArticle = 
            """
            DELETE
              FROM tbl_article
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var command = new NpgsqlCommand(sqlDeleteArticle, connection);
        command.Parameters.AddWithValue("Id", articleId);

        int result;

        try{
            result = await command.ExecuteNonQueryAsync();
        }
        catch(NpgsqlException ex){
            throw new BadRequestException("Error", new Dictionary<string, string[]>());
        }

        return result == 0 ? throw new NotFoundException("Id", $"{articleId}") : new Article();
    }

    public async Task<Article?> UpdateArticle(Article article)
    {
        const string sqlUpdateArticle = 
            """
               UPDATE tbl_article
                  SET creator_id = @CreatorId
                    , title = @Title
                    , content = @Content
                    , modified = @Modified
                WHERE id = @Id
            RETURNING created, id
            """;
        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlUpdateArticle, connection);

        var time = DateTime.Now;

        cmd.Parameters.AddWithValue("Id", article.Id);
        cmd.Parameters.AddWithValue("CreatorId", article.CreatorId);
        cmd.Parameters.AddWithValue("Title", article.Title);
        cmd.Parameters.AddWithValue("Content", article.Content);
        cmd.Parameters.AddWithValue("Modified", time);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync()){
                var articleId = reader.GetInt64(reader.GetOrdinal("id"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                return new Article(){
                    Id = articleId,
                    CreatorId = article.CreatorId,
                    Title = article.Title,
                    Content = article.Content,
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
            new NotFoundException("Id", $"{article.Id}");
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async void AddTagsToArticle(long articleId, IEnumerable<long> tagIds)
    {
        const string sqlAddTagsToArticle =
            """
            INSERT INTO m2m_article_tag (article_id, tag_id)
            SELECT @ArticleId, unnest(@TagIds)
            """;
        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlAddTagsToArticle, connection);
        
        cmd.Parameters.AddWithValue("ArticleId", articleId);
        cmd.Parameters.AddWithValue("TagIds", tagIds.ToArray()); 

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

    public async Task<IEnumerable<Article?>?> GetAllArticle()
    {
        const string sqlGetAllArticles =
            """
            SELECT id
                 , creator_id
                 , title
                 , content
                 , created
                 , modified
              FROM tbl_article
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetAllArticles, connection);
        
            using var reader = await cmd.ExecuteReaderAsync();

            var articles = new List<Article>();

            while (await reader.ReadAsync()){
                var articleId = reader.GetInt64(reader.GetOrdinal("id"));
                var creatorId = reader.GetInt64(reader.GetOrdinal("creator_id"));
                var title = reader.GetString(reader.GetOrdinal("title"));
                var content = reader.GetString(reader.GetOrdinal("content"));
                var created = reader.GetDateTime(reader.GetOrdinal("created"));
                var modified = reader.GetDateTime(reader.GetOrdinal("modified"));

                articles.Add(new Article{
                    Id = articleId,
                    CreatorId = creatorId,
                    Title = title,
                    Content = content,
                    Created = created,
                    Modified = modified
                });
            }
            return articles;
    }
}