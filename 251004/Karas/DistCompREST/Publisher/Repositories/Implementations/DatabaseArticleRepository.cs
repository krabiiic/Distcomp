using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseArticleRepository : BaseDatabaseRepository<Article>, IArticleRepository
{
    public DatabaseArticleRepository(AppDbContext context) : base(context)
    {
    }
}