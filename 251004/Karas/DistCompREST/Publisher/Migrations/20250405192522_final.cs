using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class final : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_ArticleMark",
                table: "ArticleMark");

            migrationBuilder.DropIndex(
                name: "IX_ArticleMark_MarksId",
                table: "ArticleMark");

            migrationBuilder.AddPrimaryKey(
                name: "PK_ArticleMark",
                table: "ArticleMark",
                columns: new[] { "MarksId", "StoriesId" });

            migrationBuilder.CreateIndex(
                name: "IX_ArticleMark_StoriesId",
                table: "ArticleMark",
                column: "StoriesId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_ArticleMark",
                table: "ArticleMark");

            migrationBuilder.DropIndex(
                name: "IX_ArticleMark_StoriesId",
                table: "ArticleMark");

            migrationBuilder.AddPrimaryKey(
                name: "PK_ArticleMark",
                table: "ArticleMark",
                columns: new[] { "StoriesId", "MarksId" });

            migrationBuilder.CreateIndex(
                name: "IX_ArticleMark_MarksId",
                table: "ArticleMark",
                column: "MarksId");
        }
    }
}
