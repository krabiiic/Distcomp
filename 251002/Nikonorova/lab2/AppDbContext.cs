using lab2_jpa.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace lab2_jpa
{
    public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
    {
        public DbSet<Creator> Creators { get; set; } = null!;
        public DbSet<Tag> Tags { get; set; } = null!;
        public DbSet<Article> Articles { get; set; } = null!;
        public DbSet<Note> Notes { get; set; } = null!;


    }
}
