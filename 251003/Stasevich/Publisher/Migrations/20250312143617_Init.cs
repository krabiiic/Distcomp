using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace WebApplication1.Migrations
{
    /// <inheritdoc />
    public partial class Init : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_article_tbl_user_UserId",
                schema: "distcomp",
                table: "tbl_article");

            migrationBuilder.RenameColumn(
                name: "UserId",
                schema: "distcomp",
                table: "tbl_article",
                newName: "userId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_UserId",
                schema: "distcomp",
                table: "tbl_article",
                newName: "IX_tbl_article_userId");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_article_tbl_user_userId",
                schema: "distcomp",
                table: "tbl_article",
                column: "userId",
                principalSchema: "distcomp",
                principalTable: "tbl_user",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_article_tbl_user_userId",
                schema: "distcomp",
                table: "tbl_article");

            migrationBuilder.RenameColumn(
                name: "userId",
                schema: "distcomp",
                table: "tbl_article",
                newName: "UserId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_article_userId",
                schema: "distcomp",
                table: "tbl_article",
                newName: "IX_tbl_article_UserId");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_article_tbl_user_UserId",
                schema: "distcomp",
                table: "tbl_article",
                column: "UserId",
                principalSchema: "distcomp",
                principalTable: "tbl_user",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
