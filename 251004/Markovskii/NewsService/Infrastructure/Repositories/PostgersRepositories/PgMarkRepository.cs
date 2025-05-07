using System.Runtime.InteropServices.JavaScript;
using Domain.Entities;
using Domain.Exceptions;
using Domain.Repository;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Infrastructure.Repositories.PostgersRepositories;

public class PgMarkRepository : PgRepository,IMarkRepository
{
    public PgMarkRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {

    }
    public async Task<Mark?> AddMark(Mark mark)
    {
        const string sqlInsertMark =
            """
               INSERT 
                 INTO tbl_mark
                      (
                        name
                      )
               VALUES (@Name)
            RETURNING id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlInsertMark, connection);
        cmd.Parameters.AddWithValue("Name", mark.Name);

        try{
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleResult);
            
            if (await reader.ReadAsync())
            {
                var markId = reader.GetInt64(0);
                return new Mark { Id = markId, Name = mark.Name };
            }
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<Mark?> GetMark(long markId)
    {
        const string sqlGetMarkerById =
            """
            SELECT name
                 , id
              FROM tbl_mark
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetMarkerById, connection);
        cmd.Parameters.AddWithValue("Id", markId);

        try{
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync())
            {
                var markerId = reader.GetInt64(reader.GetOrdinal("id"));
                var markerName = reader.GetString(reader.GetOrdinal("name"));

                return new Mark { Id = markerId, Name = markerName };
            }
            else
            {
                throw new NotFoundException("Id", $"{markId}");
            }
        }
        catch(NpgsqlException ex){
            new BadRequestException("Error", new Dictionary<string, string[]>());
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());;
    }

    public async Task<Mark?> RemoveMark(long markId)
    {
        const string sqlDeleteMark = 
               """
               DELETE
                 FROM tbl_mark
                WHERE id = @Id
               """;
       
               await using var connection = await GetConnection();
       
               using var command = new NpgsqlCommand(sqlDeleteMark, connection);
               command.Parameters.AddWithValue("Id", markId);
       
               int result;
               
                   result = await command.ExecuteNonQueryAsync();

        
       
                   return result == 0 ? throw new NotFoundException("Id", $"{markId}") : new Mark();
    }

    public async Task<Mark?> UpdateMark(Mark mark)
    {
        const string sqlUpdateMark = 
            """
               UPDATE tbl_mark
                  SET name = @Name
                WHERE id = @Id
            RETURNING id
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlUpdateMark, connection);

        cmd.Parameters.AddWithValue("Name", mark.Name);
        cmd.Parameters.AddWithValue("Id", mark.Id);

        try {
            var returnedId = await cmd.ExecuteScalarAsync();

            if (returnedId != null && (long)returnedId == mark.Id)
            {
                return new Mark{
                    Id = (long)returnedId,
                    Name = mark.Name
                };
            }
            
        }
        catch(NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        catch (NpgsqlException ex) when (ex.SqlState == "23503")
        {
            new NotFoundException("Id", $"{mark.Id}");
        }
        throw new BadRequestException("Error", new Dictionary<string, string[]>());
    }

    public async Task<IEnumerable<Mark?>?> GetAllMarks()
    {
        const string sqlGetMarkers = 
            """
            SELECT name
                 , id
              FROM tbl_mark
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlGetMarkers, connection);
    
    
    
            using var reader = await cmd.ExecuteReaderAsync();

            var marks = new List<Mark>();

            while (await reader.ReadAsync())
            {
                var marker = new Mark
                {
                    Id = reader.GetInt64(reader.GetOrdinal("id")),
                    Name = reader.GetString(reader.GetOrdinal("name"))
                };

                marks.Add(marker);
            }

            return marks;
    }

    public async Task<IEnumerable<Mark?>?> GetMarksCreateIfNotExist(IEnumerable<string> marks)
    {
        const string createIfNotExist = 
            """
            INSERT INTO tbl_mark (name)
            SELECT unnest(@Names)
            ON CONFLICT (name) DO UPDATE
            SET name = EXCLUDED.name
            RETURNING id;
            """;
        
            await using var connection = await GetConnection();
            using var cmd = new NpgsqlCommand(createIfNotExist, connection);
            cmd.Parameters.AddWithValue("Names", marks.ToArray());
            try
            {
                var reader = await cmd.ExecuteReaderAsync();
                var returnedIds = new List<long>();
                while (await reader.ReadAsync())
                {
                    returnedIds.Add(reader.GetInt64(reader.GetOrdinal("id")));
                }
                return returnedIds?.Select(id => new Mark() {Id = id});

            }
            catch
            {
                throw new BadRequestException("Error", new Dictionary<string, string[]>());
            }
    }
}