using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class rename_columns_for_task : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_article_tbl_editor_EditorId",
                table: "tbl_article");

            migrationBuilder.RenameColumn(
                name: "Id",
                table: "tbl_editor",
                newName: "id");

            migrationBuilder.RenameColumn(
                name: "Id",
                table: "tbl_mark",
                newName: "id");

            migrationBuilder.RenameColumn(
                name: "Id",
                table: "tbl_article",
                newName: "id");

            migrationBuilder.RenameColumn(
                name: "EditorId",
                table: "tbl_article",
                newName: "editor_id");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_EditorId",
                table: "tbl_article",
                newName: "IX_tbl_article_editor_id");

            migrationBuilder.RenameColumn(
                name: "Id",
                table: "tbl_post",
                newName: "id");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_article_tbl_editor_editor_id",
                table: "tbl_article",
                column: "editor_id",
                principalTable: "tbl_editor",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_article_tbl_editor_editor_id",
                table: "tbl_article");

            migrationBuilder.RenameColumn(
                name: "id",
                table: "tbl_editor",
                newName: "Id");

            migrationBuilder.RenameColumn(
                name: "id",
                table: "tbl_mark",
                newName: "Id");

            migrationBuilder.RenameColumn(
                name: "id",
                table: "tbl_article",
                newName: "Id");

            migrationBuilder.RenameColumn(
                name: "editor_id",
                table: "tbl_article",
                newName: "EditorId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_editor_id",
                table: "tbl_article",
                newName: "IX_tbl_article_EditorId");

            migrationBuilder.RenameColumn(
                name: "id",
                table: "tbl_post",
                newName: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_article_tbl_editor_EditorId",
                table: "tbl_article",
                column: "EditorId",
                principalTable: "tbl_editor",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
