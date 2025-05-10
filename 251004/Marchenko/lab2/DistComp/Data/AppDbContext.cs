using DistComp.Models;
using Microsoft.EntityFrameworkCore;

namespace DistComp.Data;

public class AppDbContext : DbContext
{
    public DbSet<Creator> Creators { get; set; }
    public DbSet<Issue> Issues { get; set; }
    public DbSet<Message> Messages { get; set; }
    public DbSet<Mark> Marks { get; set; }
    
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) 
    { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Creator>()
            .HasIndex(u => u.Login)
            .IsUnique();

        modelBuilder.Entity<Creator>()
            .ToTable("tbl_creator");
        
        modelBuilder.Entity<Mark>()
            .ToTable("tbl_mark");
        
        modelBuilder.Entity<Issue>()
            .ToTable("tbl_issue");
        
        modelBuilder.Entity<Message>()
            .ToTable("tbl_message");
        
        modelBuilder.Entity<Issue>()
            .HasIndex(s => s.Title)
            .IsUnique();

        modelBuilder.Entity<Mark>()
            .HasIndex(t => t.Name)
            .IsUnique();
    }
}