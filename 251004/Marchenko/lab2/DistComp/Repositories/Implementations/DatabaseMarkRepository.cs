using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace DistComp.Repositories.Implementations;

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