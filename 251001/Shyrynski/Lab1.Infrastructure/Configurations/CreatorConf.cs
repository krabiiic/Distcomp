using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Configurations
{
    public class CreatorConf : IEntityTypeConfiguration<CreatorEntity>
    {
        public void Configure(EntityTypeBuilder<CreatorEntity> builder)
        {
            builder.ToTable("tbl_creator");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Id).HasColumnName("id");
            builder.HasIndex(e => e.Login).IsUnique();
            builder.Property(e => e.Password).IsRequired();
            builder.Property(e => e.FirstName).IsRequired();
            builder.Property(e => e.LastName).IsRequired();
        }
    }
}
