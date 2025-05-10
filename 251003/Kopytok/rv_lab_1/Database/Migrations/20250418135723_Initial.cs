using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Database.Migrations
{
    /// <inheritdoc />
    public partial class Initial : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.EnsureSchema(
                name: "public");

            migrationBuilder.CreateTable(
                name: "tbl_editor",
                schema: "public",
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
                    table.PrimaryKey("PK_tbl_editor", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_tag",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    name = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_tag", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_story",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    editorId = table.Column<long>(type: "bigint", nullable: false),
                    title = table.Column<string>(type: "text", nullable: false),
                    content = table.Column<string>(type: "text", nullable: false),
                    created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_story", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_story_tbl_editor_editorId",
                        column: x => x.editorId,
                        principalSchema: "public",
                        principalTable: "tbl_editor",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_note",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    storyId = table.Column<long>(type: "bigint", nullable: false),
                    content = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_note", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_note_tbl_story_storyId",
                        column: x => x.storyId,
                        principalSchema: "public",
                        principalTable: "tbl_story",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_story_tag",
                schema: "public",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    storyId = table.Column<long>(type: "bigint", nullable: false),
                    tagId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_story_tag", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_story_tag_tbl_story_storyId",
                        column: x => x.storyId,
                        principalSchema: "public",
                        principalTable: "tbl_story",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_tbl_story_tag_tbl_tag_tagId",
                        column: x => x.tagId,
                        principalSchema: "public",
                        principalTable: "tbl_tag",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_note_storyId",
                schema: "public",
                table: "tbl_note",
                column: "storyId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_story_editorId",
                schema: "public",
                table: "tbl_story",
                column: "editorId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_story_tag_storyId",
                schema: "public",
                table: "tbl_story_tag",
                column: "storyId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_story_tag_tagId",
                schema: "public",
                table: "tbl_story_tag",
                column: "tagId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_note",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_story_tag",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_story",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_tag",
                schema: "public");

            migrationBuilder.DropTable(
                name: "tbl_editor",
                schema: "public");
        }
    }
}
