using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class Unique_Indexes : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateIndex(
                name: "IX_Editors_Login",
                table: "Editors",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Marks_Name",
                table: "Marks",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Stories_Title",
                table: "Stories",
                column: "Title",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Editors_Login",
                table: "Editors");

            migrationBuilder.DropIndex(
                name: "IX_Marks_Name",
                table: "Marks");

            migrationBuilder.DropIndex(
                name: "IX_Stories_Title",
                table: "Stories");
        }
    }
}
