using Microsoft.EntityFrameworkCore;
using Lab1.Infrastructure.Entities;
using Lab1.Infrastructure.Configurations;
namespace Lab1.Infrastructure.Contexts
{
    public class DataContext : DbContext
    {
        // DbSet — коллекции сущностей, которые отображаются на таблицы в базе данных
        public DbSet<NewsEntity> News { get; set; }
        public DbSet<UserEntity> Users { get; set; }
        public DbSet<TagEntity> Tags { get; set; }
//        public DbSet<PostEntity> Posts { get; set; }
        public DbSet<NewsTagEntity> NewsTags { get; set; }

        public DataContext(DbContextOptions<DataContext> options) : base(options) { }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Вызывается при создании модели БД для задания настроек
            // Настраивает маппинг сущностей на таблицы с помощью классов конфигураций
            base.OnModelCreating(modelBuilder);
            modelBuilder.ApplyConfiguration(new NewsConf());
            modelBuilder.ApplyConfiguration(new UserConf());
            modelBuilder.ApplyConfiguration(new TagConf());
//            modelBuilder.ApplyConfiguration(new PostConf());
            modelBuilder.ApplyConfiguration(new NewsTagConf());
        }

    }
}
