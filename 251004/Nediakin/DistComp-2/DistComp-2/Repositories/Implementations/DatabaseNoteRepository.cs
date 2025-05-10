using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseNoteRepository : BaseDatabaseRepository<Note>, INoteRepository
{
    public DatabaseNoteRepository(AppDbContext context) : base(context)
    {
    }
}