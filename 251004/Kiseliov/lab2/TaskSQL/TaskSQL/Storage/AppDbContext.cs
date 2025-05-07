using Microsoft.EntityFrameworkCore;
using TaskSQL.Models;

namespace TaskSQL.Storage;

public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
{
    public DbSet<Creator> Creators { get; set; } = null!;
    public DbSet<Tag> Tags { get; set; } = null!;
    public DbSet<Tweet> Tweets { get; set; } = null!;
    public DbSet<Notice> Notices { get; set; } = null!;
}