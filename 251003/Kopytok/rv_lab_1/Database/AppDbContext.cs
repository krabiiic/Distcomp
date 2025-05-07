using Core;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace Database
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Editor> Editors { get; set; }
        public DbSet<Story> Stories { get; set; }
        public DbSet<Tag> Tags { get; set; }
        public DbSet<StoryTag> StoryTags { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasDefaultSchema("public");

            modelBuilder.Entity<Editor>().ToTable("tbl_editor");
            modelBuilder.Entity<Story>().ToTable("tbl_story");
            modelBuilder.Entity<Tag>().ToTable("tbl_tag");
            modelBuilder.Entity<StoryTag>().ToTable("tbl_story_tag");

            modelBuilder.Entity<Story>()
            .HasOne(s => s.Editor)
            .WithMany(e => e.Stories)
            .HasForeignKey(s => s.EditorId);

            modelBuilder.Entity<StoryTag>()
            .HasOne(s => s.Story)
            .WithMany(e => e.StoryTags)
            .HasForeignKey(s => s.StoryId);

            modelBuilder.Entity<StoryTag>()
            .HasOne(s => s.Tag)
            .WithMany(e => e.StoryTags)
            .HasForeignKey(s => s.TagId);
        }
    }
}
