//using Lab1.Infrastructure.Entities;
//using Microsoft.EntityFrameworkCore.Metadata.Builders;
//using Microsoft.EntityFrameworkCore;
//namespace Lab1.Infrastructure.Configurations
//{
//    public class PostConf : IEntityTypeConfiguration<PostEntity>
//    {
//        public void Configure(EntityTypeBuilder<PostEntity> builder)
//        {
//            builder.ToTable("tbl_post");
//            builder.HasKey(e => e.Id);
//            builder.Property(e => e.Id).HasColumnName("id");
//            builder.Property(e => e.Content).IsRequired();
//            builder.Property(e => e.NewsId).HasColumnName("news_id");
//            builder
//                .HasOne(i => i.News)
//                .WithMany(c => c.Posts)
//                .HasForeignKey(i => i.NewsId)
//                .OnDelete(DeleteBehavior.Cascade);
//        }
//    }
//}
