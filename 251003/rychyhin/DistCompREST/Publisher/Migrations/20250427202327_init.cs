using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class init : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_editor",
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
                    table.PrimaryKey("PK_tbl_editor", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_label",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    name = table.Column<string>(type: "text", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_label", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_news",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Title = table.Column<string>(type: "text", maxLength: 64, nullable: false),
                    Content = table.Column<string>(type: "text", maxLength: 2048, nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    editor_id = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_news", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_news_tbl_editor_editor_id",
                        column: x => x.editor_id,
                        principalTable: "tbl_editor",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "LabelNews",
                columns: table => new
                {
                    LabelsId = table.Column<long>(type: "bigint", nullable: false),
                    StoriesId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_LabelNews", x => new { x.LabelsId, x.StoriesId });
                    table.ForeignKey(
                        name: "FK_LabelNews_tbl_label_LabelsId",
                        column: x => x.LabelsId,
                        principalTable: "tbl_label",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_LabelNews_tbl_news_StoriesId",
                        column: x => x.StoriesId,
                        principalTable: "tbl_news",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_LabelNews_StoriesId",
                table: "LabelNews",
                column: "StoriesId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_editor_Login",
                table: "tbl_editor",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_label_name",
                table: "tbl_label",
                column: "name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_news_editor_id",
                table: "tbl_news",
                column: "editor_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_news_Title",
                table: "tbl_news",
                column: "Title",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "LabelNews");

            migrationBuilder.DropTable(
                name: "tbl_label");

            migrationBuilder.DropTable(
                name: "tbl_news");

            migrationBuilder.DropTable(
                name: "tbl_editor");
        }
    }
}
