
using Microsoft.EntityFrameworkCore;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
namespace Lab1.Infrastructure.Configurations
{
    public class IssueConf : IEntityTypeConfiguration<IssueEntity>
    {
        public void Configure(EntityTypeBuilder<IssueEntity> builder)
        {
            builder.ToTable("tbl_issue");
            builder.HasKey(e => e.Id);
            builder.Property(e => e.Id).HasColumnName("id");
            builder.HasIndex(e => e.Title).IsUnique();
            builder.Property(e => e.Content).IsRequired();
            builder.Property(e => e.Created).IsRequired();
            builder.Property(e => e.Modified).IsRequired();
            builder.Property(e => e.CreatorId).HasColumnName("creator_id");
            builder
                .HasOne(i => i.Creator)
                .WithMany(c => c.Issues)
                .HasForeignKey(i => i.CreatorId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
