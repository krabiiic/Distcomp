using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Configurations
{
    public class TagConf : IEntityTypeConfiguration<TagEntity>
    {
        public void Configure(EntityTypeBuilder<TagEntity> builder)
        {
            builder.ToTable("tbl_tag");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Id).HasColumnName("id");
            builder.HasIndex(e => e.Name).IsUnique();
            builder.Property(e => e.Name).HasColumnName("name");
        }
    }
}
