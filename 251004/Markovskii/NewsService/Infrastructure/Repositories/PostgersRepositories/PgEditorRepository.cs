using System.Runtime.InteropServices.JavaScript;
using Domain.Entities;
using Domain.Exceptions;
using Domain.Repository;
using Microsoft.Extensions.Options;
using Npgsql;

namespace Infrastructure.Repositories.PostgersRepositories;

public class PgEditorRepository : PgRepository, IEditorRepository
{
    public PgEditorRepository(IOptions<InfrastructureSettings> settings) : base(settings.Value)
    {

    }

    public async Task<Editor?> AddEditor(Editor editor)
    {
        const string sqlCreateUser =
            """
               INSERT 
                 INTO tbl_editor
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

        cmd.Parameters.AddWithValue("Login", editor.Login);
        cmd.Parameters.AddWithValue("Password", editor.Password);
        cmd.Parameters.AddWithValue("Firstname", editor.Firstname);
        cmd.Parameters.AddWithValue("Lastname", editor.Lastname);

        try
        {
            using var reader = await cmd.ExecuteReaderAsync(System.Data.CommandBehavior.SingleRow);
            if (await reader.ReadAsync())
            {
                var editorId = reader.GetInt64(0);
                return new Editor()
                {
                    Id = editorId,
                    Login = editor.Login,
                    Password = editor.Password,
                    Firstname = editor.Firstname,
                    Lastname = editor.Lastname
                };
            }
        } catch(NpgsqlException ex) when (ex.SqlState == "23505")
        {
            throw new AlreadyExistsException();
        }
        throw new NotFoundException("Id", $"{editor.Id}");
    }

    public async Task<Editor?> GetEditor(long editorId)
    {
        const string sqlGetUserById = 
            """
            SELECT id
                 , login
                 , password
                 , firstname
                 , lastname
              FROM tbl_editor
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetUserById, connection);
        cmd.Parameters.AddWithValue("Id", editorId);
        
            var reader = await cmd.ExecuteReaderAsync();

            if (await reader.ReadAsync()){
                var editorLogin = reader.GetString(reader.GetOrdinal("login"));
                var editorPassword = reader.GetString(reader.GetOrdinal("password"));
                var editorFirstname = reader.GetString(reader.GetOrdinal("firstname"));
                var editorLastname = reader.GetString(reader.GetOrdinal("lastname"));

                return new Editor(){
                    Id = editorId,
                    Login = editorLogin,
                    Password = editorPassword,
                    Firstname = editorFirstname,
                    Lastname = editorLastname
                };
            }
            throw new NotFoundException("Id", $"{editorId}");
    }

    public async Task<Editor?> RemoveEditor(long editorId)
    {
        const string sqlDeleteUser =
            """
            DELETE
              FROM tbl_editor
             WHERE id = @Id
            """;

        await using var connection = await GetConnection();
        await using var cmd = new NpgsqlCommand(sqlDeleteUser, connection);

        cmd.Parameters.AddWithValue("Id", editorId);

        int result;
        result = await cmd.ExecuteNonQueryAsync();

        if (result == 0)
        {
            throw new NotFoundException("Id",editorId.ToString());
        }
        return new Editor();
    }
    

    public async Task<Editor?> UpdateEditor(Editor editor)
    {
        const string sqlUpdateUser = 
            """
               UPDATE tbl_editor
                  SET login = @Login
                    , password = @Password
                    , firstname = @Firstname
                    , lastname = @Lastname
                WHERE id = @Id
            RETURNING id
            """;

        await using var connection = await GetConnection();
        using var cmd = new NpgsqlCommand(sqlUpdateUser, connection);

        cmd.Parameters.AddWithValue("Id", editor.Id);
        cmd.Parameters.AddWithValue("Login", editor.Login);
        cmd.Parameters.AddWithValue("Password", editor.Password);
        cmd.Parameters.AddWithValue("Firstname", editor.Firstname);
        cmd.Parameters.AddWithValue("Lastname", editor.Lastname);
        
            var returnedId = await cmd.ExecuteScalarAsync();

            if(returnedId != null && (long) returnedId == editor.Id){
                return new Editor(){
                    Id = (long)returnedId,
                    Login = editor.Login,
                    Password = editor.Password,
                    Firstname = editor.Firstname,
                    Lastname = editor.Lastname
                };
            }
            throw new NotFoundException("Id", $"{editor.Id}");  
    }

    public async Task<IEnumerable<Editor?>?> GetAllEditors()
    {
        const string sqlGetUsers = 
            """
            SELECT id
                 , login
                 , password
                 , firstname
                 , lastname
              FROM tbl_editor
            """;

        await using var connection = await GetConnection();

        using var cmd = new NpgsqlCommand(sqlGetUsers, connection);


            using var reader = await cmd.ExecuteReaderAsync();

            var editors = new List<Editor>();

            while (await reader.ReadAsync()){
                var editorId = reader.GetInt64(reader.GetOrdinal("id"));
                var editorLogin = reader.GetString(reader.GetOrdinal("login"));
                var editorPassword = reader.GetString(reader.GetOrdinal("password"));
                var editorFirstname = reader.GetString(reader.GetOrdinal("firstname"));
                var editorLastname = reader.GetString(reader.GetOrdinal("lastname"));

                editors.Add(new Editor(){
                    Id = editorId,
                    Login = editorLogin,
                    Password = editorPassword,
                    Firstname = editorFirstname,
                    Lastname = editorLastname
                });
            }
            return editors;
    }
}