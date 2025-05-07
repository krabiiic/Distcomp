using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Database.Migrations
{
    /// <inheritdoc />
    public partial class StoryFix : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_story_tbl_editor_editorId",
                schema: "public",
                table: "tbl_story");

            migrationBuilder.RenameColumn(
                name: "editorId",
                schema: "public",
                table: "tbl_story",
                newName: "editor_id");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_story_editorId",
                schema: "public",
                table: "tbl_story",
                newName: "IX_tbl_story_editor_id");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_story_tbl_editor_editor_id",
                schema: "public",
                table: "tbl_story",
                column: "editor_id",
                principalSchema: "public",
                principalTable: "tbl_editor",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_story_tbl_editor_editor_id",
                schema: "public",
                table: "tbl_story");

            migrationBuilder.RenameColumn(
                name: "editor_id",
                schema: "public",
                table: "tbl_story",
                newName: "editorId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_story_editor_id",
                schema: "public",
                table: "tbl_story",
                newName: "IX_tbl_story_editorId");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_story_tbl_editor_editorId",
                schema: "public",
                table: "tbl_story",
                column: "editorId",
                principalSchema: "public",
                principalTable: "tbl_editor",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
