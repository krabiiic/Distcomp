using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class SwitchedModels4 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Name",
                table: "tbl_tag",
                newName: "name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_tag_Name",
                table: "tbl_tag",
                newName: "IX_tbl_tag_name");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "name",
                table: "tbl_tag",
                newName: "Name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_tag_name",
                table: "tbl_tag",
                newName: "IX_tbl_tag_Name");
        }
    }
}
