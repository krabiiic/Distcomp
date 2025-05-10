using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace TaskSQL.Migrations
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
                name: "tbl_tweet",
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
                    table.PrimaryKey("PK_tbl_tweet", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_tweet_tbl_creator_Creatorid",
                        column: x => x.creator_id,
                        principalTable: "tbl_creator",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "TagTweet",
                columns: table => new
                {
                    Tagsid = table.Column<long>(type: "bigint", nullable: false),
                    Tweetsid = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TagTweet", x => new { x.Tagsid, x.Tweetsid });
                    table.ForeignKey(
                        name: "FK_TagTweet_tbl_tag_Tagsid",
                        column: x => x.Tagsid,
                        principalTable: "tbl_tag",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_TagTweet_tbl_tweet_Tweetsid",
                        column: x => x.Tweetsid,
                        principalTable: "tbl_tweet",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_notice",
                columns: table => new
                {
                    id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Tweetid = table.Column<long>(type: "bigint", nullable: true),
                    Content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_notice", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_notice_tbl_tweet_Tweetid",
                        column: x => x.Tweetid,
                        principalTable: "tbl_tweet",
                        principalColumn: "id");
                });

            migrationBuilder.CreateIndex(
                name: "IX_TagTweet_Tweetsid",
                table: "TagTweet",
                column: "Tweetsid");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_creator_Login",
                table: "tbl_creator",
                column: "Login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_notice_Tweetid",
                table: "tbl_notice",
                column: "Tweetid");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tag_Name",
                table: "tbl_tag",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tweet_Creatorid",
                table: "tbl_tweet",
                column: "creator_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_tweet_Title",
                table: "tbl_tweet",
                column: "Title",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "TagTweet");

            migrationBuilder.DropTable(
                name: "tbl_notice");

            migrationBuilder.DropTable(
                name: "tbl_tag");

            migrationBuilder.DropTable(
                name: "tbl_tweet");

            migrationBuilder.DropTable(
                name: "tbl_creator");
        }
    }
}
