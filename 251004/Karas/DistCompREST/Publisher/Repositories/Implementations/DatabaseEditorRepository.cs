using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class DatabaseEditorRepository : BaseDatabaseRepository<Editor>, IEditorRepository
{
    public DatabaseEditorRepository(AppDbContext context) : base(context)
    {
        
    }
}