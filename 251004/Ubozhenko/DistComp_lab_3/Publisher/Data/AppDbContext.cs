using Microsoft.EntityFrameworkCore;
using Publisher.Models;

namespace Publisher.Data;

public class AppDbContext : DbContext
{
    public DbSet<Author> Authors { get; set; }
    public DbSet<News> News { get; set; }
    public DbSet<Label> Labels { get; set; }
    
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) 
    { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Author>()
            .HasIndex(u => u.Login)
            .IsUnique();

        modelBuilder.Entity<Author>()
            .ToTable("tbl_author");
        
        modelBuilder.Entity<Label>()
            .ToTable("tbl_label");
        
        modelBuilder.Entity<News>()
            .ToTable("tbl_news");
        
        modelBuilder.Entity<News>()
            .HasIndex(s => s.Title)
            .IsUnique();

        modelBuilder.Entity<Label>()
            .HasIndex(t => t.Name)
            .IsUnique();
    }
}