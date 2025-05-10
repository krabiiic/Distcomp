using DistComp.Data;
using DistComp.Exceptions;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseStickerRepository : BaseDatabaseRepository<Sticker>, IStickerRepository
{
    public DatabaseStickerRepository(AppDbContext context) : base(context)
    {
    }
}