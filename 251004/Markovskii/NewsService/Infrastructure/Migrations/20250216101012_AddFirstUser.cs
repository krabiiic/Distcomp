using System;
using FluentMigrator;

namespace Infrastructure.Migrations;

[Migration(20250216101012, TransactionBehavior.None)]
public class AddFirstUser : Migration
{
    public override void Up()
    {
        const string add_editor_script = 
"""
INSERT 
  INTO tbl_editor 
       (
        login, password, firstname, lastname
       ) 
VALUES 
       (
        'ilia5556523ilia@mail.ru', 'password', 'Илья', 'Марковский'
       ) 
    ON CONFLICT (login) 
    DO NOTHING
    ;
""";
        Execute.Sql(add_editor_script);
    }

    public override void Down()
    {
    }
}
