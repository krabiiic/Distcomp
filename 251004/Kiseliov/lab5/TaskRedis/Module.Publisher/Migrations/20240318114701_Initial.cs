using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Publisher.Migrations
{
    /// <inheritdoc />
    public partial class Initial : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_creators",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Login = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    Password = table.Column<string>(type: "character varying(128)", maxLength: 128, nullable: false),
                    FirstName = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    LastName = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_creators", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_tags",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_tags", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_tweets",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    CreatorId = table.Column<long>(type: "bigint", nullable: true),
                    Title = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false),
                    Content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false),
                    Created = table.Column<DateTime>(type: "timestamp with time zone", nullable: true),
                    Modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_tweets", x => x.Id);
                    table.ForeignKey(
                        name: "FK_tbl_tweets_tbl_creators_CreatorId",
                        column: x => x.CreatorId,
                        principalTable: "tbl_creators",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateTable(
                name: "TagTweet",
                columns: table => new
                {
                    TagsId = table.Column<long>(type: "bigint", nullable: false),
                    TweetsId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TagTweet", x => new { x.TagsId, x.TweetsId });
                    table.ForeignKey(
                        name: "FK_TagTweet_tbl_tags_TagsId",
                        column: x => x.TagsId,
                        principalTable: "tbl_tags",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_TagTweet_tbl_tweets_TweetsId",
                        column: x => x.TweetsId,
                        principalTable: "tbl_tweets",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_notices",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    TweetId = table.Column<long>(type: "bigint", nullable: true),
                    Content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_notices", x => x.Id);
                    table.ForeignKey(
                        name: "FK_tbl_notices_tbl_tweets_TweetId",
                        column: x => x.TweetId,
                        principalTable: "tbl_tweets",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateIndex(
                name: "IX_TagTweet_TweetsId",
                table: "TagTweet",
                column: "TweetsId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_creators_Login",
                table: "tbl_creators",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_notices_TweetId",
                table: "tbl_notices",
                column: "TweetId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tags_Name",
                table: "tbl_tags",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tweets_CreatorId",
                table: "tbl_tweets",
                column: "CreatorId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tweets_Title",
                table: "tbl_tweets",
                column: "Title",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "TagTweet");

            migrationBuilder.DropTable(
                name: "tbl_notices");

            migrationBuilder.DropTable(
                name: "tbl_tags");

            migrationBuilder.DropTable(
                name: "tbl_tweets");

            migrationBuilder.DropTable(
                name: "tbl_creators");
        }
    }
}
