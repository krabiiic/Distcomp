using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace WebApplication1.Migrations
{
    /// <inheritdoc />
    public partial class RenameColumns : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.EnsureSchema(
                name: "distcomp");

            migrationBuilder.CreateTable(
                name: "tbl_sticker",
                schema: "distcomp",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    name = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_sticker", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_user",
                schema: "distcomp",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    login = table.Column<string>(type: "text", nullable: false),
                    password = table.Column<string>(type: "text", nullable: false),
                    firstname = table.Column<string>(type: "text", nullable: false),
                    lastname = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_user", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_article",
                schema: "distcomp",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    title = table.Column<string>(type: "text", nullable: false),
                    content = table.Column<string>(type: "text", nullable: false),
                    created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_article", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_article_tbl_user_UserId",
                        column: x => x.UserId,
                        principalSchema: "distcomp",
                        principalTable: "tbl_user",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_article_sticker",
                schema: "distcomp",
                columns: table => new
                {
                    article_id = table.Column<long>(type: "bigint", nullable: false),
                    sticker_id = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_article_sticker", x => new { x.article_id, x.sticker_id });
                    table.ForeignKey(
                        name: "fk_article_sticker_article",
                        column: x => x.article_id,
                        principalSchema: "distcomp",
                        principalTable: "tbl_article",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "fk_article_sticker_sticker",
                        column: x => x.sticker_id,
                        principalSchema: "distcomp",
                        principalTable: "tbl_sticker",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_note",
                schema: "distcomp",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    ArticleId = table.Column<long>(type: "bigint", nullable: false),
                    content = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_note", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_note_tbl_article_ArticleId",
                        column: x => x.ArticleId,
                        principalSchema: "distcomp",
                        principalTable: "tbl_article",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_UserId",
                schema: "distcomp",
                table: "tbl_article",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_sticker_sticker_id",
                schema: "distcomp",
                table: "tbl_article_sticker",
                column: "sticker_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_note_ArticleId",
                schema: "distcomp",
                table: "tbl_note",
                column: "ArticleId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_article_sticker",
                schema: "distcomp");

            migrationBuilder.DropTable(
                name: "tbl_note",
                schema: "distcomp");

            migrationBuilder.DropTable(
                name: "tbl_sticker",
                schema: "distcomp");

            migrationBuilder.DropTable(
                name: "tbl_article",
                schema: "distcomp");

            migrationBuilder.DropTable(
                name: "tbl_user",
                schema: "distcomp");
        }
    }
}
