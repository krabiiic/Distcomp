using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Configurations
{
    public class IssueStickerConf : IEntityTypeConfiguration<IssueStickerEntity>
    {
        public void Configure(EntityTypeBuilder<IssueStickerEntity> builder)
        {
            builder.HasKey(s=>s.Id);
            builder.Property(e => e.Id).HasColumnName("id");

            builder
                .HasOne(s => s.Issue)
                .WithMany(i => i.IssueStickers)
                .HasForeignKey(s => s.IssueId).OnDelete(DeleteBehavior.Cascade);

            builder
                .HasOne(s => s.Sticker)
                .WithMany(st => st.IssueStickers)
                .HasForeignKey(s => s.StickerId).OnDelete(DeleteBehavior.Cascade);
        }
    }
}
