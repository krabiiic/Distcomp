using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class FixedNamingOfFK : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_issue_tbl_creator_CreatorId",
                table: "tbl_issue");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_message_tbl_issue_IssueId",
                table: "tbl_message");

            migrationBuilder.RenameColumn(
                name: "IssueId",
                table: "tbl_message",
                newName: "issue_id");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_message_IssueId",
                table: "tbl_message",
                newName: "IX_tbl_message_issue_id");

            migrationBuilder.RenameColumn(
                name: "CreatorId",
                table: "tbl_issue",
                newName: "creator_id");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_issue_CreatorId",
                table: "tbl_issue",
                newName: "IX_tbl_issue_creator_id");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_issue_tbl_creator_creator_id",
                table: "tbl_issue",
                column: "creator_id",
                principalTable: "tbl_creator",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_message_tbl_issue_issue_id",
                table: "tbl_message",
                column: "issue_id",
                principalTable: "tbl_issue",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_issue_tbl_creator_creator_id",
                table: "tbl_issue");

            migrationBuilder.DropForeignKey(
                name: "FK_tbl_message_tbl_issue_issue_id",
                table: "tbl_message");

            migrationBuilder.RenameColumn(
                name: "issue_id",
                table: "tbl_message",
                newName: "IssueId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_message_issue_id",
                table: "tbl_message",
                newName: "IX_tbl_message_IssueId");

            migrationBuilder.RenameColumn(
                name: "creator_id",
                table: "tbl_issue",
                newName: "CreatorId");

            migrationBuilder.RenameIndex(
                name: "IX_tbl_issue_creator_id",
                table: "tbl_issue",
                newName: "IX_tbl_issue_CreatorId");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_issue_tbl_creator_CreatorId",
                table: "tbl_issue",
                column: "CreatorId",
                principalTable: "tbl_creator",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_message_tbl_issue_IssueId",
                table: "tbl_message",
                column: "IssueId",
                principalTable: "tbl_issue",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
