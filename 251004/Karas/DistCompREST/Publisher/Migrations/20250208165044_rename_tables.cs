using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class rename_tables : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Posts_Stories_ArticleId",
                table: "Posts");

            migrationBuilder.DropForeignKey(
                name: "FK_Stories_Editors_EditorId",
                table: "Stories");

            migrationBuilder.DropForeignKey(
                name: "FK_ArticleMark_Stories_StoriesId",
                table: "ArticleMark");

            migrationBuilder.DropForeignKey(
                name: "FK_ArticleMark_Marks_MarksId",
                table: "ArticleMark");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Editors",
                table: "Editors");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Marks",
                table: "Marks");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Stories",
                table: "Stories");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Posts",
                table: "Posts");

            migrationBuilder.RenameTable(
                name: "Editors",
                newName: "tbl_editor");

            migrationBuilder.RenameTable(
                name: "Marks",
                newName: "tbl_mark");

            migrationBuilder.RenameTable(
                name: "Stories",
                newName: "tbl_article");

            migrationBuilder.RenameTable(
                name: "Posts",
                newName: "tbl_post");

            migrationBuilder.RenameIndex(
                name: "IX_Editors_Login",
                table: "tbl_editor",
                newName: "IX_tbl_editor_Login");

            migrationBuilder.RenameIndex(
                name: "IX_Marks_Name",
                table: "tbl_mark",
                newName: "IX_tbl_mark_Name");

            migrationBuilder.RenameIndex(
                name: "IX_Stories_EditorId",
                table: "tbl_article",
                newName: "IX_tbl_article_EditorId");

            migrationBuilder.RenameIndex(
                name: "IX_Stories_Title",
                table: "tbl_article",
                newName: "IX_tbl_article_Title");

            migrationBuilder.RenameIndex(
                name: "IX_Posts_ArticleId",
                table: "tbl_post",
                newName: "IX_tbl_post_ArticleId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_editor",
                table: "tbl_editor",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_mark",
                table: "tbl_mark",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_article",
                table: "tbl_article",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_post",
                table: "tbl_post",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_ArticleMark_tbl_article_StoriesId",
                table: "ArticleMark",
                column: "StoriesId",
                principalTable: "tbl_article",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_ArticleMark_tbl_mark_MarksId",
                table: "ArticleMark",
                column: "MarksId",
                principalTable: "tbl_mark",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_post_tbl_article_ArticleId",
                table: "tbl_post",
                column: "ArticleId",
                principalTable: "tbl_article",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_article_tbl_editor_EditorId",
                table: "tbl_article",
                column: "EditorId",
                principalTable: "tbl_editor",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ArticleMark_tbl_article_StoriesId",
                table: "ArticleMark");

            migrationBuilder.DropForeignKey(
                name: "FK_ArticleMark_tbl_mark_MarksId",
                table: "ArticleMark");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_post_tbl_article_ArticleId",
                table: "tbl_post");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_article_tbl_editor_EditorId",
                table: "tbl_article");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_editor",
                table: "tbl_editor");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_mark",
                table: "tbl_mark");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_article",
                table: "tbl_article");

            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_post",
                table: "tbl_post");

            migrationBuilder.RenameTable(
                name: "tbl_editor",
                newName: "Editors");

            migrationBuilder.RenameTable(
                name: "tbl_mark",
                newName: "Marks");

            migrationBuilder.RenameTable(
                name: "tbl_article",
                newName: "Stories");

            migrationBuilder.RenameTable(
                name: "tbl_post",
                newName: "Posts");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_editor_Login",
                table: "Editors",
                newName: "IX_Editors_Login");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_mark_Name",
                table: "Marks",
                newName: "IX_Marks_Name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_EditorId",
                table: "Stories",
                newName: "IX_Stories_EditorId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_Title",
                table: "Stories",
                newName: "IX_Stories_Title");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_post_ArticleId",
                table: "Posts",
                newName: "IX_Posts_ArticleId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Editors",
                table: "Editors",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Marks",
                table: "Marks",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Stories",
                table: "Stories",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Posts",
                table: "Posts",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_Stories_ArticleId",
                table: "Posts",
                column: "ArticleId",
                principalTable: "Stories",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_Stories_Editors_EditorId",
                table: "Stories",
                column: "EditorId",
                principalTable: "Editors",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_ArticleMark_Stories_StoriesId",
                table: "ArticleMark",
                column: "StoriesId",
                principalTable: "Stories",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_ArticleMark_Marks_MarksId",
                table: "ArticleMark",
                column: "MarksId",
                principalTable: "Marks",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
