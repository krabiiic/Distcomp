using Microsoft.EntityFrameworkCore;
using Publisher.Models;

namespace Publisher.Data;

public class AppDbContext : DbContext
{
    public DbSet<Editor> Editors { get; set; }
    public DbSet<Article> Stories { get; set; }
    public DbSet<Mark> Marks { get; set; }
    
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
        
        modelBuilder.Entity<Mark>()
            .ToTable("tbl_mark");
        
        modelBuilder.Entity<Article>()
            .ToTable("tbl_article");
        
        modelBuilder.Entity<Article>()
            .HasIndex(s => s.Title)
            .IsUnique();

        modelBuilder.Entity<Mark>()
            .HasIndex(t => t.Name)
            .IsUnique();
    }
}