using System.Runtime.InteropServices.JavaScript;
using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Core.Repositories.PostgersRepositories;

public class PgTagRepository : PgRepository,ITagRepository
{
    public PgTagRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {

    }
    public async Task<Tag?> AddTag(Tag tag)
    {
        const string sqlInsertTag =
            """
               INSERT 
                 INTO tbl_tag
                      (
                        name
                      )
               VALUES (@Name)
            RETURNING id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlInsertTag, connection);
        cmd.Parameters.AddWithValue("Name", tag.Name);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleResult);
            
            if (await reader.ReadAsync())
            {
                var tagId = reader.GetInt64(0);
                return new Tag { Id = tagId, Name = tag.Name };
            }
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<Tag?> GetTag(long tagId)
    {
        const string sqlGetTagById =
            """
            SELECT name
                 , id
              FROM tbl_tag
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetTagById, connection);
        cmd.Parameters.AddWithValue("Id", tagId);

        try{
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync())
            {
                var tag_Id = reader.GetInt64(reader.GetOrdinal("id"));
                var tag_Name = reader.GetString(reader.GetOrdinal("name"));

                return new Tag { Id = tag_Id, Name = tag_Name };
            }
            else
            {
                throw new NotFoundException("Id", $"{tagId}");
            }
        }
        catch(NpgsqlException ex){
            new BadRequestException("Error", new Dictionary<string, string[]>());
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());;
    }

    public async Task<Tag?> RemoveTag(long tagId)
    {
        const string sqlDeleteTag =
               """
               DELETE
                 FROM tbl_tag
                WHERE id = @Id
               """;
       
               await using var connection = await GetConnection();
       
               using var command = new NpgsqlCommand(sqlDeleteTag, connection);
               command.Parameters.AddWithValue("Id", tagId);
       
               int result;
               
                   result = await command.ExecuteNonQueryAsync();

        
       
                   return result == 0 ? throw new NotFoundException("Id", $"{tagId}") : new Tag();
    }

    public async Task<Tag?> UpdateTag(Tag tag)
    {
        const string sqlUpdateTag =
            """
               UPDATE tbl_tag
                  SET name = @Name
                WHERE id = @Id
            RETURNING id
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlUpdateTag, connection);

        cmd.Parameters.AddWithValue("Name", tag.Name);
        cmd.Parameters.AddWithValue("Id", tag.Id);

        try {
            var returnedId = await cmd.ExecuteScalarAsync();

            if (returnedId != null && (long)returnedId == tag.Id)
            {
                return new Tag{
                    Id = (long)returnedId,
                    Name = tag.Name
                };
            }
            
        }
        catch(NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23503")
        {
            new NotFoundException("Id", $"{tag.Id}");
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<IEnumerable<Tag?>?> GetAllTag()
    {
        const string sqlGetTags =
            """
            SELECT name
                 , id
              FROM tbl_tag
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlGetTags, connection);
    
    
    
            using var reader = await cmd.ExecuteReaderAsync();

            var tags = new List<Tag>();

            while (await reader.ReadAsync())
            {
                var tag = new Tag
                {
                    Id = reader.GetInt64(reader.GetOrdinal("id")),
                    Name = reader.GetString(reader.GetOrdinal("name"))
                };

                tags.Add(tag);
            }

            return tags;
    }

    public async Task<IEnumerable<Tag?>?> GetTagsCreateIfNotExist(IEnumerable<string> tags)
    {
        const string createIfNotExist =
            """
            INSERT INTO tbl_tag (name)
            SELECT unnest(@Names)
            ON CONFLICT (name) DO UPDATE
            SET name = EXCLUDED.name
            RETURNING id;
            """;
        
            await using var connection = await GetConnection();
            using var cmd = new NpgsqlCommand(createIfNotExist, connection);
            cmd.Parameters.AddWithValue("Names", tags.ToArray());
            try
            {
                var reader = await cmd.ExecuteReaderAsync();
                var returnedIds = new List<long>();
                while (await reader.ReadAsync())
                {
                    returnedIds.Add(reader.GetInt64(reader.GetOrdinal("id")));
                }
                return returnedIds?.Select(id => new Tag() {Id = id});

            }
            catch
            {
                throw new BadRequestException("Error", new Dictionary<string, string[]>());
            }
    }
}