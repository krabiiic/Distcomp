using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Configurations
{
    public class NewsTagConf : IEntityTypeConfiguration<NewsTagEntity>
    {
        public void Configure(EntityTypeBuilder<NewsTagEntity> builder)
        {
            builder.HasKey(s=>s.Id);
            builder.Property(e => e.Id).HasColumnName("id");

            builder
                .HasOne(s => s.News)
                .WithMany(i => i.NewsTags)
                .HasForeignKey(s => s.NewsId).OnDelete(DeleteBehavior.Cascade);

            builder
                .HasOne(s => s.Tag)
                .WithMany(st => st.NewsTags)
                .HasForeignKey(s => s.TagId).OnDelete(DeleteBehavior.Cascade); 
        }
    }
}
