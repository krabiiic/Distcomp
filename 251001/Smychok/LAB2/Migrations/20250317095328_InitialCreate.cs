using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace LAB2.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.EnsureSchema(
                name: "public");

            migrationBuilder.CreateTable(
                name: "tbl_sticker",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    name = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_sticker", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_writer",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    login = table.Column<string>(type: "varchar(64)", maxLength: 64, nullable: false),
                    password = table.Column<string>(type: "varchar(128)", nullable: false),
                    firstname = table.Column<string>(type: "varchar(64)", nullable: false),
                    lastname = table.Column<string>(type: "varchar(64)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_writer", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_issue",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    writer_id = table.Column<int>(type: "integer", nullable: false),
                    title = table.Column<string>(type: "text", nullable: false),
                    content = table.Column<string>(type: "text", nullable: false),
                    created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_issue", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_issue_tbl_writer_writer_id",
                        column: x => x.writer_id,
                        principalSchema: "public",
                        principalTable: "tbl_writer",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_issue_sticker",
                schema: "public",
                columns: table => new
                {
                    issueId = table.Column<int>(type: "integer", nullable: false),
                    stickerId = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_issue_sticker", x => new { x.issueId, x.stickerId });
                    table.ForeignKey(
                        name: "FK_tbl_issue_sticker_tbl_issue_issueId",
                        column: x => x.issueId,
                        principalSchema: "public",
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_tbl_issue_sticker_tbl_sticker_stickerId",
                        column: x => x.stickerId,
                        principalSchema: "public",
                        principalTable: "tbl_sticker",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_post",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    content = table.Column<string>(type: "text", nullable: false),
                    issueId = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_post", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_post_tbl_issue_issueId",
                        column: x => x.issueId,
                        principalSchema: "public",
                        principalTable: "tbl_issue",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_writer_id",
                schema: "public",
                table: "tbl_issue",
                column: "writer_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_sticker_stickerId",
                schema: "public",
                table: "tbl_issue_sticker",
                column: "stickerId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_post_issueId",
                schema: "public",
                table: "tbl_post",
                column: "issueId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_writer_login",
                schema: "public",
                table: "tbl_writer",
                column: "login",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_issue_sticker",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_post",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_sticker",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_issue",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_writer",
                schema: "public");
        }
    }
}
