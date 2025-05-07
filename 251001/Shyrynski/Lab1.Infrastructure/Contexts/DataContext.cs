using Microsoft.EntityFrameworkCore;
using Lab1.Infrastructure.Entities;
using Lab1.Infrastructure.Configurations;
namespace Lab1.Infrastructure.Contexts
{
    public class DataContext : DbContext
    {
        public DbSet<IssueEntity> Issues { get; set; }
        public DbSet<CreatorEntity> Creators { get; set; }
        public DbSet<StickerEntity> Stickers { get; set; }
        public DbSet<IssueStickerEntity> IssueStickers { get; set; }
        public DataContext(DbContextOptions<DataContext> options) : base(options) { }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.ApplyConfiguration(new IssueConf());
            modelBuilder.ApplyConfiguration(new CreatorConf());
            modelBuilder.ApplyConfiguration(new StickerConf());
            modelBuilder.ApplyConfiguration(new IssueStickerConf());
        }
    }
}
