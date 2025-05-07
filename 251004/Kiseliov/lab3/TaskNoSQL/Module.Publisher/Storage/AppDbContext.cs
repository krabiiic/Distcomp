using Microsoft.EntityFrameworkCore;
using Publisher.Models;
namespace Publisher.Storage;

public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
{
    public DbSet<Creator> Creators { get; set; } = null!;
    public DbSet<Tag> Tags { get; set; } = null!;
    public DbSet<Tweet> Tweets { get; set; } = null!;
}