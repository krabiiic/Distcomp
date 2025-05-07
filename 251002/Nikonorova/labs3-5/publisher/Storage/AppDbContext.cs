using Microsoft.EntityFrameworkCore;
using publisher.Models;
using static System.Net.Mime.MediaTypeNames;

namespace publisher.Storage
{
    public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
    {
        public DbSet<Creator> Creators { get; set; } = null!;
        public DbSet<Tag> Tags { get; set; } = null!;
        public DbSet<Article> Articles { get; set; } = null!;

        //
        //public DbSet<Note> Notes { get; set; } = null!;
    }
}
