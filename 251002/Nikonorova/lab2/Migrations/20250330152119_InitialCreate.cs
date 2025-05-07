using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace lab2_jpa.Migrations
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
                    Login = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    Password = table.Column<string>(type: "character varying(128)", maxLength: 128, nullable: false),
                    FirstName = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    LastName = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_creator", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_tag",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_tag", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_article",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    creator_id = table.Column<long>(type: "bigint", nullable: true),
                    Title = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false),
                    Content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: true),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_article", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_article_tbl_creator_creator_id",
                        column: x => x.creator_id,
                        principalTable: "tbl_creator",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "ArticleTag",
                columns: table => new
                {
                    Articlesid = table.Column<long>(type: "bigint", nullable: false),
                    Tagsid = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ArticleTag", x => new { x.Articlesid, x.Tagsid });
                    table.ForeignKey(
                        name: "FK_ArticleTag_tbl_article_Articlesid",
                        column: x => x.Articlesid,
                        principalTable: "tbl_article",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_ArticleTag_tbl_tag_Tagsid",
                        column: x => x.Tagsid,
                        principalTable: "tbl_tag",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_note",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Articleid = table.Column<long>(type: "bigint", nullable: true),
                    Content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_note", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_note_tbl_article_Articleid",
                        column: x => x.Articleid,
                        principalTable: "tbl_article",
                        principalColumn: "id");
                });

            migrationBuilder.CreateIndex(
                name: "IX_ArticleTag_Tagsid",
                table: "ArticleTag",
                column: "Tagsid");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_creator_id",
                table: "tbl_article",
                column: "creator_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_Title",
                table: "tbl_article",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_creator_Login",
                table: "tbl_creator",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_note_Articleid",
                table: "tbl_note",
                column: "Articleid");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tag_Name",
                table: "tbl_tag",
                column: "Name",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "ArticleTag");

            migrationBuilder.DropTable(
                name: "tbl_note");

            migrationBuilder.DropTable(
                name: "tbl_tag");

            migrationBuilder.DropTable(
                name: "tbl_article");

            migrationBuilder.DropTable(
                name: "tbl_creator");
        }
    }
}
