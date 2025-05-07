using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace DistComp.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_creator",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Login = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Password = table.Column<string>(type: "text", maxLength: 128, nullable: false),
                    Firstname = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Lastname = table.Column<string>(type: "text", maxLength: 64, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_creator", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_mark",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "text", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_mark", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_issue",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Title = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Content = table.Column<string>(type: "text", maxLength: 2048, nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    creator_id = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_issue", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_issue_tbl_creator_creator_id",
                        column: x => x.creator_id,
                        principalTable: "tbl_creator",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "IssueMark",
                columns: table => new
                {
                    IssuesId = table.Column<long>(type: "bigint", nullable: false),
                    MarksId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_IssueMark", x => new { x.IssuesId, x.MarksId });
                    table.ForeignKey(
                        name: "FK_IssueMark_tbl_issue_IssuesId",
                        column: x => x.IssuesId,
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_IssueMark_tbl_mark_MarksId",
                        column: x => x.MarksId,
                        principalTable: "tbl_mark",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_message",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Content = table.Column<string>(type: "text", maxLength: 2048, nullable: false),
                    IssueId = table.Column<long>(type: "bigint", nullable: false)
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
                name: "IX_IssueMark_MarksId",
                table: "IssueMark",
                column: "MarksId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_creator_Login",
                table: "tbl_creator",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_creator_id",
                table: "tbl_issue",
                column: "creator_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_Title",
                table: "tbl_issue",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_mark_Name",
                table: "tbl_mark",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_message_IssueId",
                table: "tbl_message",
                column: "IssueId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "IssueMark");

            migrationBuilder.DropTable(
                name: "tbl_message");

            migrationBuilder.DropTable(
                name: "tbl_mark");

            migrationBuilder.DropTable(
                name: "tbl_issue");

            migrationBuilder.DropTable(
                name: "tbl_creator");
        }
    }
}
