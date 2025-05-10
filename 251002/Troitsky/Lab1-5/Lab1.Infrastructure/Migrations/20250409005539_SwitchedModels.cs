using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Lab1.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class SwitchedModels : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "IssueStickers");

            migrationBuilder.DropTable(
                name: "tbl_issue");

            migrationBuilder.DropTable(
                name: "tbl_sticker");

            migrationBuilder.DropTable(
                name: "tbl_creator");

            migrationBuilder.CreateTable(
                name: "tbl_tag",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Name = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_tag", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_user",
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
                    table.PrimaryKey("PK_tbl_user", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_news",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    user_id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Title = table.Column<string>(type: "text", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_news", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_news_tbl_user_user_id",
                        column: x => x.user_id,
                        principalTable: "tbl_user",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "NewsTags",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    NewsId = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    TagId = table.Column<decimal>(type: "numeric(20,0)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_NewsTags", x => x.id);
                    table.ForeignKey(
                        name: "FK_NewsTags_tbl_news_NewsId",
                        column: x => x.NewsId,
                        principalTable: "tbl_news",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_NewsTags_tbl_tag_TagId",
                        column: x => x.TagId,
                        principalTable: "tbl_tag",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

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
                name: "IX_NewsTags_NewsId",
                table: "NewsTags",
                column: "NewsId");

            migrationBuilder.CreateIndex(
                name: "IX_NewsTags_TagId",
                table: "NewsTags",
                column: "TagId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_news_Title",
                table: "tbl_news",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_news_user_id",
                table: "tbl_news",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_post_news_id",
                table: "tbl_post",
                column: "news_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tag_Name",
                table: "tbl_tag",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_user_Login",
                table: "tbl_user",
                column: "Login",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "NewsTags");

            migrationBuilder.DropTable(
                name: "tbl_post");

            migrationBuilder.DropTable(
                name: "tbl_tag");

            migrationBuilder.DropTable(
                name: "tbl_news");

            migrationBuilder.DropTable(
                name: "tbl_user");

            migrationBuilder.CreateTable(
                name: "tbl_creator",
                columns: table => new
                {
                    id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    FirstName = table.Column<string>(type: "text", nullable: false),
                    LastName = table.Column<string>(type: "text", nullable: false),
                    Login = table.Column<string>(type: "text", nullable: false),
                    Password = table.Column<string>(type: "text", nullable: false)
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
                    creator_id = table.Column<decimal>(type: "numeric(20,0)", nullable: false),
                    Content = table.Column<string>(type: "text", nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Title = table.Column<string>(type: "text", nullable: false)
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
                name: "IX_tbl_issue_creator_id",
                table: "tbl_issue",
                column: "creator_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_issue_Title",
                table: "tbl_issue",
                column: "Title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_sticker_Name",
                table: "tbl_sticker",
                column: "Name",
                unique: true);
        }
    }
}
