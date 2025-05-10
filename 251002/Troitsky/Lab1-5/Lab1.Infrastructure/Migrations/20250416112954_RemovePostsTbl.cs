using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class RemovePostsTbl : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_post");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_post",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    news_id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_post", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_post_tbl_news_news_id",
                        column: x => x.news_id,
                        principalTable: "tbl_news",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_post_news_id",
                table: "tbl_post",
                column: "news_id");
        }
    }
}
