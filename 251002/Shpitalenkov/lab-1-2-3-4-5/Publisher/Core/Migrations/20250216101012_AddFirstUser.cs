using System;
using FluentMigrator;

namespace Core.Migrations;

[Migration(20250216101012, TransactionBehavior.None)]
public class AddFirstUser : Migration
{
    public override void Up()
    {
        const string add_creator_script =
"""
INSERT 
  INTO tbl_creator 
       (
        login, password, firstname, lastname
       ) 
VALUES 
       (
        'kmjndfg@gmail.com', 'password', 'Денис', 'Шпиталенков'
       ) 
    ON CONFLICT (login) 
    DO NOTHING
    ;
""";
        Execute.Sql(add_creator_script);
    }

    public override void Down()
    {
    }
}
