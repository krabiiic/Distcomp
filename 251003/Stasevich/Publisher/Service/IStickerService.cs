using WebApplication1.DTO;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public interface IStickerService
    {
        Task<StickerResponseTo> CreateStickerAsync(StickerRequestTo dto);
        Task<StickerResponseTo> GetStickerByIdAsync(long id);
        Task<PaginatedResult<StickerResponseTo>> GetAllStickersAsync(int pageNumber = 1, int pageSize = 10);
        Task<StickerResponseTo> UpdateStickerAsync(long id, StickerRequestTo dto);
        Task DeleteStickerAsync(long id);
    }
}
