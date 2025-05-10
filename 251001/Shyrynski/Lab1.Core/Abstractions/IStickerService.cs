using Lab1.Core.Contracts;
using Lab1.Core.Models;

namespace Lab1.Core.Abstractions
{
    public interface IStickerService
    {
        StickerResponseTo? GetSticker(ulong id);
        StickerResponseTo CreateSticker(Sticker sticker);
        bool DeleteSticker(ulong id);
        List<StickerResponseTo> GetAllStickers();
        StickerResponseTo? UpdateSticker(Sticker sticker, ulong id);
    }
}
