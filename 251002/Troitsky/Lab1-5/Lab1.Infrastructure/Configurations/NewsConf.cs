
using Microsoft.EntityFrameworkCore;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
namespace Lab1.Infrastructure.Configurations
{
    public class NewsConf : IEntityTypeConfiguration<NewsEntity>
    {
        public void Configure(EntityTypeBuilder<NewsEntity> builder)
        {
            builder.ToTable("tbl_news");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Id).HasColumnName("id");
            builder.HasIndex(e => e.Title).IsUnique();
            builder.Property(e => e.Content).IsRequired();
            builder.Property(e => e.Created).IsRequired();
            builder.Property(e => e.Modified).IsRequired();
            builder.Property(e => e.UserId).HasColumnName("user_id");
            builder
                .HasOne(i => i.User)
                .WithMany(c => c.News)
                .HasForeignKey(i => i.UserId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
