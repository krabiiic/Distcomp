using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Configurations
{
    public class StickerConf : IEntityTypeConfiguration<StickerEntity>
    {
        public void Configure(EntityTypeBuilder<StickerEntity> builder)
        {
            builder.ToTable("tbl_sticker");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Id).HasColumnName("id");
            builder.HasIndex(e => e.Name).IsUnique();
        }
    }
}
