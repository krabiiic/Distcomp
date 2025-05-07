using System.Runtime.InteropServices.JavaScript;
using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Core.Repositories.PostgersRepositories;

public class PgCreatorRepository : PgRepository, ICreatorRepository
{
    public PgCreatorRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {

    }

    public async Task<Creator?> AddCreator(Creator creator)
    {
        const string sqlCreateUser =
            """
               INSERT 
                 INTO tbl_creator
                      (
                        login
                      , password
                      , firstname
                      , lastname
                      )
               VALUES (
                        @Login
                      , @Password
                      , @Firstname
                      , @Lastname
                      )
            RETURNING id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlCreateUser, connection);

        cmd.Parameters.AddWithValue("Login", creator.Login);
        cmd.Parameters.AddWithValue("Password", creator.Password);
        cmd.Parameters.AddWithValue("Firstname", creator.Firstname);
        cmd.Parameters.AddWithValue("Lastname", creator.Lastname);

        try
        {
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync())
            {
                var creatorId = reader.GetInt64(0);
                return new Creator()
                {
                    Id = creatorId,
                    Login = creator.Login,
                    Password = creator.Password,
                    Firstname = creator.Firstname,
                    Lastname = creator.Lastname
                };
            }
        } catch(NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        throw new NotFoundException("Id", $"{creator.Id}");
    }

    public async Task<Creator?> GetCreator(long creatorId)
    {
        const string sqlGetUserById =
            """
            SELECT id
                 , login
                 , password
                 , firstname
                 , lastname
              FROM tbl_creator
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetUserById, connection);
        cmd.Parameters.AddWithValue("Id", creatorId);
        
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync()){
                var creatorLogin = reader.GetString(reader.GetOrdinal("login"));
                var creatorPassword = reader.GetString(reader.GetOrdinal("password"));
                var creatorFirstname = reader.GetString(reader.GetOrdinal("firstname"));
                var creatorLastname = reader.GetString(reader.GetOrdinal("lastname"));

                return new Creator(){
                    Id = creatorId,
                    Login = creatorLogin,
                    Password = creatorPassword,
                    Firstname = creatorFirstname,
                    Lastname = creatorLastname
                };
            }
            throw new NotFoundException("Id", $"{creatorId}");
    }

    public async Task<Creator?> RemoveCreator(long creatorId)
    {
        const string sqlDeleteUser =
            """
            DELETE
              FROM tbl_creator
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlDeleteUser, connection);

        cmd.Parameters.AddWithValue("Id", creatorId);

        int result;
        result = await cmd.ExecuteNonQueryAsync();

        if (result == 0)
        {
            throw new NotFoundException("Id",creatorId.ToString());
        }
        return new Creator();
    }
    

    public async Task<Creator?> UpdateCreator(Creator creator)
    {
        const string sqlUpdateUser =
            """
               UPDATE tbl_creator
                  SET login = @Login
                    , password = @Password
                    , firstname = @Firstname
                    , lastname = @Lastname
                WHERE id = @Id
            RETURNING id
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlUpdateUser, connection);

        cmd.Parameters.AddWithValue("Id", creator.Id);
        cmd.Parameters.AddWithValue("Login", creator.Login);
        cmd.Parameters.AddWithValue("Password", creator.Password);
        cmd.Parameters.AddWithValue("Firstname", creator.Firstname);
        cmd.Parameters.AddWithValue("Lastname", creator.Lastname);
        
            var returnedId = await cmd.ExecuteScalarAsync();

            if(returnedId != null && (long) returnedId == creator.Id){
                return new Creator(){
                    Id = (long)returnedId,
                    Login = creator.Login,
                    Password = creator.Password,
                    Firstname = creator.Firstname,
                    Lastname = creator.Lastname
                };
            }
            throw new NotFoundException("Id", $"{creator.Id}");  
    }

    public async Task<IEnumerable<Creator?>?> GetAllCreators()
    {
        const string sqlGetUsers =
            """
            SELECT id
                 , login
                 , password
                 , firstname
                 , lastname
              FROM tbl_creator
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetUsers, connection);


            using var reader = await cmd.ExecuteReaderAsync();

            var creators = new List<Creator>();

            while (await reader.ReadAsync()){
                var creatorId = reader.GetInt64(reader.GetOrdinal("id"));
                var creatorLogin = reader.GetString(reader.GetOrdinal("login"));
                var creatorPassword = reader.GetString(reader.GetOrdinal("password"));
                var creatorFirstname = reader.GetString(reader.GetOrdinal("firstname"));
                var creatorLastname = reader.GetString(reader.GetOrdinal("lastname"));

                creators.Add(new Creator(){
                    Id = creatorId,
                    Login = creatorLogin,
                    Password = creatorPassword,
                    Firstname = creatorFirstname,
                    Lastname = creatorLastname
                });
            }
            return creators;
    }
}