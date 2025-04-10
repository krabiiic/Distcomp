using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IStickerService
{
    Task<IEnumerable<StickerResponseDTO>> GetStickersAsync();

    Task<StickerResponseDTO> GetStickerByIdAsync(long id);

    Task<StickerResponseDTO> CreateStickerAsync(StickerRequestDTO sticker);

    Task<StickerResponseDTO> UpdateStickerAsync(StickerRequestDTO sticker);

    Task DeleteStickerAsync(long id);
}