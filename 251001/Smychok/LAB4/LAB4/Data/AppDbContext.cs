using Microsoft.EntityFrameworkCore.Infrastructure;

namespace LAB4.Data;

using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations.Schema;
using Models;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.HasDefaultSchema("public"); // Указываем схему по умолчанию
        
        modelBuilder.Entity<Writer>(e =>
        {
            e.ToTable("tbl_writer");
            e.HasKey(w => w.id);
            e.Property(w => w.login).HasMaxLength(64);
            e.HasIndex(w => w.login).IsUnique();

            // Каскадное удаление для Writer
            e.HasMany(w => w.issues)
                .WithOne(i => i.writer)
                .HasForeignKey(i => i.writer_id)
                .OnDelete(DeleteBehavior.Cascade);
        });
        
        modelBuilder.Entity<Issue>(e =>
        {
            e.ToTable("tbl_issue");

            // Связь с Writer
            e.HasOne(i => i.writer)
                .WithMany(w => w.issues)
                .HasForeignKey(i => i.writer_id)
                .OnDelete(DeleteBehavior.Cascade); // Каскадное удаление для Writer

            // Связь многие-ко-многим с Sticker
            e.HasMany(i => i.stickers)
                .WithMany(s => s.issues)
                .UsingEntity<IssueSticker>(
                    j => j.HasOne(iss => iss.sticker)
                        .WithMany()
                        .HasForeignKey(iss => iss.stickerId)
                        .OnDelete(DeleteBehavior.Cascade), // Каскадное удаление для Sticker
                    j => j.HasOne(iss => iss.issue)
                        .WithMany()
                        .HasForeignKey(iss => iss.issueId)
                        .OnDelete(DeleteBehavior.Cascade), // Каскадное удаление для Issue
                    j => j.ToTable("tbl_issue_sticker")
                );
        });
        
        modelBuilder.Entity<Post>(e => 
        {
            e.ToTable("tbl_post");
            e.HasOne(p => p.issue)
                .WithMany(i => i.posts)
                .HasForeignKey(p => p.issueId);
        });
        
        modelBuilder.Entity<Sticker>(e => 
        {
            e.ToTable("tbl_sticker");
            e.Property(s => s.name).HasMaxLength(32);
        });
        
        modelBuilder.Entity<IssueSticker>(e => 
        {
            e.ToTable("tbl_issue_sticker");
            e.HasKey(iss => new { IssueId = iss.issueId, StickerId = iss.stickerId });
        });
    }

    public DbSet<Writer> Writers => Set<Writer>();
    public DbSet<Issue> Issues => Set<Issue>();
    public DbSet<Post> Posts => Set<Post>();
    public DbSet<Sticker> Stickers => Set<Sticker>();
}