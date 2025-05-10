using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DistComp.Migrations
{
    /// <inheritdoc />
    public partial class Lowercase_Name_In_Label_Model : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Name",
                table: "tbl_label",
                newName: "name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_label_Name",
                table: "tbl_label",
                newName: "IX_tbl_label_name");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "name",
                table: "tbl_label",
                newName: "Name");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_label_name",
                table: "tbl_label",
                newName: "IX_tbl_label_Name");
        }
    }
}
