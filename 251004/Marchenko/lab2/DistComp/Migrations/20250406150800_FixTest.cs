using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DistComp.Migrations
{
    /// <inheritdoc />
    public partial class FixTest : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Name",
                table: "tbl_mark",
                newName: "name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_mark_Name",
                table: "tbl_mark",
                newName: "IX_tbl_mark_name");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "name",
                table: "tbl_mark",
                newName: "Name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_mark_name",
                table: "tbl_mark",
                newName: "IX_tbl_mark_Name");
        }
    }
}
