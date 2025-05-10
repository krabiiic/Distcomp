using Microsoft.EntityFrameworkCore;
using Publisher.Models;

namespace Publisher.Data;

public class AppDbContext : DbContext
{
    public DbSet<Editor> Editors { get; set; }
    public DbSet<News> Stories { get; set; }
    public DbSet<Label> Labels { get; set; }
    
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) 
    { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Editor>()
            .HasIndex(u => u.Login)
            .IsUnique();

        modelBuilder.Entity<Editor>()
            .ToTable("tbl_editor");
        
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