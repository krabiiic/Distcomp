using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class DeletedMessageEntity : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_message");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_message",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    issue_id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_message", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_message_tbl_issue_issue_id",
                        column: x => x.issue_id,
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_message_issue_id",
                table: "tbl_message",
                column: "issue_id");
        }
    }
}
