using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IStickerService
{
    Task<IEnumerable<StickerResponseDTO>> GetStickersAsync();

    Task<StickerResponseDTO> GetStickerByIdAsync(long id);

    Task<StickerResponseDTO> CreateStickerAsync(StickerRequestDTO sticker);

    Task<StickerResponseDTO> UpdateStickerAsync(StickerRequestDTO sticker);

    Task DeleteStickerAsync(long id);
}