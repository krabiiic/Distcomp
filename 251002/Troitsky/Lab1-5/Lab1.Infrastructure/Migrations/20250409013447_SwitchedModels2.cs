using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class SwitchedModels2 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_NewsTags_tbl_news_NewsId",
                table: "NewsTags");

            migrationBuilder.DropForeignKey(
                name: "FK_NewsTags_tbl_tag_TagId",
                table: "NewsTags");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_news_tbl_user_user_id",
                table: "tbl_news");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_post_tbl_news_news_id",
                table: "tbl_post");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_user",
                table: "tbl_user");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_tag",
                table: "tbl_tag");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_post",
                table: "tbl_post");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_news",
                table: "tbl_news");

            migrationBuilder.DropPrimaryKey(
                name: "PK_NewsTags",
                table: "NewsTags");

            migrationBuilder.RenameTable(
                name: "NewsTags",
                newName: "news_tags");

            migrationBuilder.RenameColumn(
                name: "Password",
                table: "tbl_user",
                newName: "password");

            migrationBuilder.RenameColumn(
                name: "Login",
                table: "tbl_user",
                newName: "login");

            migrationBuilder.RenameColumn(
                name: "LastName",
                table: "tbl_user",
                newName: "last_name");

            migrationBuilder.RenameColumn(
                name: "FirstName",
                table: "tbl_user",
                newName: "first_name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_user_Login",
                table: "tbl_user",
                newName: "ix_tbl_user_login");

            migrationBuilder.RenameColumn(
                name: "Name",
                table: "tbl_tag",
                newName: "name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_tag_Name",
                table: "tbl_tag",
                newName: "ix_tbl_tag_name");

            migrationBuilder.RenameColumn(
                name: "Content",
                table: "tbl_post",
                newName: "content");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_post_news_id",
                table: "tbl_post",
                newName: "ix_tbl_post_news_id");

            migrationBuilder.RenameColumn(
                name: "Title",
                table: "tbl_news",
                newName: "title");

            migrationBuilder.RenameColumn(
                name: "Modified",
                table: "tbl_news",
                newName: "modified");

            migrationBuilder.RenameColumn(
                name: "Created",
                table: "tbl_news",
                newName: "created");

            migrationBuilder.RenameColumn(
                name: "Content",
                table: "tbl_news",
                newName: "content");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_news_user_id",
                table: "tbl_news",
                newName: "ix_tbl_news_user_id");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_news_Title",
                table: "tbl_news",
                newName: "ix_tbl_news_title");

            migrationBuilder.RenameColumn(
                name: "TagId",
                table: "news_tags",
                newName: "tag_id");

            migrationBuilder.RenameColumn(
                name: "NewsId",
                table: "news_tags",
                newName: "news_id");

            migrationBuilder.RenameIndex(
                name: "IX_NewsTags_TagId",
                table: "news_tags",
                newName: "ix_news_tags_tag_id");

            migrationBuilder.RenameIndex(
                name: "IX_NewsTags_NewsId",
                table: "news_tags",
                newName: "ix_news_tags_news_id");

            migrationBuilder.AddPrimaryKey(
                name: "pk_tbl_user",
                table: "tbl_user",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "pk_tbl_tag",
                table: "tbl_tag",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "pk_tbl_post",
                table: "tbl_post",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "pk_tbl_news",
                table: "tbl_news",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "pk_news_tags",
                table: "news_tags",
                column: "id");

            migrationBuilder.AddForeignKey(
                name: "fk_news_tags_news_news_id",
                table: "news_tags",
                column: "news_id",
                principalTable: "tbl_news",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "fk_news_tags_tags_tag_id",
                table: "news_tags",
                column: "tag_id",
                principalTable: "tbl_tag",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "fk_tbl_news_users_user_id",
                table: "tbl_news",
                column: "user_id",
                principalTable: "tbl_user",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "fk_tbl_post_tbl_news_news_id",
                table: "tbl_post",
                column: "news_id",
                principalTable: "tbl_news",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "fk_news_tags_news_news_id",
                table: "news_tags");

            migrationBuilder.DropForeignKey(
                name: "fk_news_tags_tags_tag_id",
                table: "news_tags");

            migrationBuilder.DropForeignKey(
                name: "fk_tbl_news_users_user_id",
                table: "tbl_news");

            migrationBuilder.DropForeignKey(
                name: "fk_tbl_post_tbl_news_news_id",
                table: "tbl_post");

            migrationBuilder.DropPrimaryKey(
                name: "pk_tbl_user",
                table: "tbl_user");

            migrationBuilder.DropPrimaryKey(
                name: "pk_tbl_tag",
                table: "tbl_tag");

            migrationBuilder.DropPrimaryKey(
                name: "pk_tbl_post",
                table: "tbl_post");

            migrationBuilder.DropPrimaryKey(
                name: "pk_tbl_news",
                table: "tbl_news");

            migrationBuilder.DropPrimaryKey(
                name: "pk_news_tags",
                table: "news_tags");

            migrationBuilder.RenameTable(
                name: "news_tags",
                newName: "NewsTags");

            migrationBuilder.RenameColumn(
                name: "password",
                table: "tbl_user",
                newName: "Password");

            migrationBuilder.RenameColumn(
                name: "login",
                table: "tbl_user",
                newName: "Login");

            migrationBuilder.RenameColumn(
                name: "last_name",
                table: "tbl_user",
                newName: "LastName");

            migrationBuilder.RenameColumn(
                name: "first_name",
                table: "tbl_user",
                newName: "FirstName");

            migrationBuilder.RenameIndex(
                name: "ix_tbl_user_login",
                table: "tbl_user",
                newName: "IX_tbl_user_Login");

            migrationBuilder.RenameColumn(
                name: "name",
                table: "tbl_tag",
                newName: "Name");

            migrationBuilder.RenameIndex(
                name: "ix_tbl_tag_name",
                table: "tbl_tag",
                newName: "IX_tbl_tag_Name");

            migrationBuilder.RenameColumn(
                name: "content",
                table: "tbl_post",
                newName: "Content");

            migrationBuilder.RenameIndex(
                name: "ix_tbl_post_news_id",
                table: "tbl_post",
                newName: "IX_tbl_post_news_id");

            migrationBuilder.RenameColumn(
                name: "title",
                table: "tbl_news",
                newName: "Title");

            migrationBuilder.RenameColumn(
                name: "modified",
                table: "tbl_news",
                newName: "Modified");

            migrationBuilder.RenameColumn(
                name: "created",
                table: "tbl_news",
                newName: "Created");

            migrationBuilder.RenameColumn(
                name: "content",
                table: "tbl_news",
                newName: "Content");

            migrationBuilder.RenameIndex(
                name: "ix_tbl_news_user_id",
                table: "tbl_news",
                newName: "IX_tbl_news_user_id");

            migrationBuilder.RenameIndex(
                name: "ix_tbl_news_title",
                table: "tbl_news",
                newName: "IX_tbl_news_Title");

            migrationBuilder.RenameColumn(
                name: "tag_id",
                table: "NewsTags",
                newName: "TagId");

            migrationBuilder.RenameColumn(
                name: "news_id",
                table: "NewsTags",
                newName: "NewsId");

            migrationBuilder.RenameIndex(
                name: "ix_news_tags_tag_id",
                table: "NewsTags",
                newName: "IX_NewsTags_TagId");

            migrationBuilder.RenameIndex(
                name: "ix_news_tags_news_id",
                table: "NewsTags",
                newName: "IX_NewsTags_NewsId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_user",
                table: "tbl_user",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_tag",
                table: "tbl_tag",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_post",
                table: "tbl_post",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_news",
                table: "tbl_news",
                column: "id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_NewsTags",
                table: "NewsTags",
                column: "id");

            migrationBuilder.AddForeignKey(
                name: "FK_NewsTags_tbl_news_NewsId",
                table: "NewsTags",
                column: "NewsId",
                principalTable: "tbl_news",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_NewsTags_tbl_tag_TagId",
                table: "NewsTags",
                column: "TagId",
                principalTable: "tbl_tag",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_news_tbl_user_user_id",
                table: "tbl_news",
                column: "user_id",
                principalTable: "tbl_user",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_post_tbl_news_news_id",
                table: "tbl_post",
                column: "news_id",
                principalTable: "tbl_news",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
