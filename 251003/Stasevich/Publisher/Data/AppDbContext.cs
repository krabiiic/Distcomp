using Microsoft.EntityFrameworkCore;
using WebApplication1.Entity;

namespace WebApplication1.Data
{
    public class AppDbContext : DbContext
    {
        public DbSet<User> Users { get; set; } = null!;
        public DbSet<Article> Articles { get; set; } = null!;
        public DbSet<Note> Notes { get; set; } = null!;
        public DbSet<Sticker> Stickers { get; set; } = null!;

        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasDefaultSchema("distcomp");

            modelBuilder.Entity<User>(entity =>
            {
                entity.ToTable("tbl_user");
                entity.HasKey(u => u.Id);
                entity.Property(u => u.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(u => u.Firstname).HasColumnName("firstname");
                entity.Property(u => u.Lastname).HasColumnName("lastname");
                entity.Property(u => u.Login).HasColumnName("login");
                entity.Property(u => u.Password).HasColumnName("password");
            });

            modelBuilder.Entity<Article>(entity =>
            {
                entity.ToTable("tbl_article");
                entity.HasKey(a => a.Id);
                entity.Property(a => a.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(a => a.UserId).HasColumnName("user_id");
                entity.Property(a => a.Title).HasColumnName("title");
                entity.Property(a => a.Content).HasColumnName("content");
                entity.Property(a => a.Created).HasColumnName("created");
                entity.Property(a => a.Modified).HasColumnName("modified");
                entity.HasOne(a => a.User)
                      .WithMany(u => u.Articles)
                      .HasForeignKey(a => a.UserId);
            });

            modelBuilder.Entity<Note>(entity =>
            {
                entity.ToTable("tbl_note");
                entity.HasKey(n => n.Id);
                entity.Property(n => n.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(n => n.Content).HasColumnName("content");
                entity.HasOne(n => n.Article)
                      .WithMany(a => a.Notes)
                      .HasForeignKey(n => n.ArticleId);
            });

            modelBuilder.Entity<Sticker>(entity =>
            {
                entity.ToTable("tbl_sticker");
                entity.HasKey(s => s.Id);
                entity.Property(s => s.Id).HasColumnName("id").ValueGeneratedOnAdd();
                entity.Property(s => s.Name).HasColumnName("name");
            });

            modelBuilder.Entity<Article>()
                .HasMany(a => a.Stickers)
                .WithMany(s => s.Articles)
                .UsingEntity<Dictionary<string, object>>(
                    "tbl_article_sticker",
                    j => {
                        return j.HasOne<Sticker>()
                                .WithMany()
                                .HasForeignKey("sticker_id")
                                .HasConstraintName("fk_article_sticker_sticker");
                    },
                    j => {
                        return j.HasOne<Article>()
                                .WithMany()
                                .HasForeignKey("article_id")
                                .HasConstraintName("fk_article_sticker_article");
                    },
                    j =>
                    {
                        j.HasKey("article_id", "sticker_id");
                        j.ToTable("tbl_article_sticker", schema: "distcomp");
                    }
                );
        }
    }
}
