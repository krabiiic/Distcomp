using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class InitCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_creator",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Login = table.Column<string>(type: "text", nullable: false),
                    Password = table.Column<string>(type: "text", nullable: false),
                    FirstName = table.Column<string>(type: "text", nullable: false),
                    LastName = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_creator", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_sticker",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Name = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_sticker", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_issue",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    CreatorId = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Title = table.Column<string>(type: "text", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_issue", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_issue_tbl_creator_CreatorId",
                        column: x => x.CreatorId,
                        principalTable: "tbl_creator",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "IssueStickers",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    IssueId = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    StickerId = table.Column<decimal>(type: "numeric(20,0)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_IssueStickers", x => x.id);
                    table.ForeignKey(
                        name: "FK_IssueStickers_tbl_issue_IssueId",
                        column: x => x.IssueId,
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_IssueStickers_tbl_sticker_StickerId",
                        column: x => x.StickerId,
                        principalTable: "tbl_sticker",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_message",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    IssueId = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_message", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_message_tbl_issue_IssueId",
                        column: x => x.IssueId,
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_IssueStickers_IssueId",
                table: "IssueStickers",
                column: "IssueId");

            migrationBuilder.CreateIndex(
                name: "IX_IssueStickers_StickerId",
                table: "IssueStickers",
                column: "StickerId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_creator_Login",
                table: "tbl_creator",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_CreatorId",
                table: "tbl_issue",
                column: "CreatorId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_Title",
                table: "tbl_issue",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_message_IssueId",
                table: "tbl_message",
                column: "IssueId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_sticker_Name",
                table: "tbl_sticker",
                column: "Name",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "IssueStickers");

            migrationBuilder.DropTable(
                name: "tbl_message");

            migrationBuilder.DropTable(
                name: "tbl_sticker");

            migrationBuilder.DropTable(
                name: "tbl_issue");

            migrationBuilder.DropTable(
                name: "tbl_creator");
        }
    }
}
