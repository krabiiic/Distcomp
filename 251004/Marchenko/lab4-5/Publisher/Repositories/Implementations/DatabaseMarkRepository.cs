using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace Publisher.Repositories.Implementations;

public class DatabaseMarkRepository : BaseDatabaseRepository<Mark>, IMarkRepository
{
    public DatabaseMarkRepository(AppDbContext context) : base(context)
    {
    }
    
    public async Task<IEnumerable<Mark>> GetByNamesAsync(IEnumerable<string> names)
    {
        return await _context.Marks
            .Where(m => names.Contains(m.Name))
            .ToListAsync();
    }
}