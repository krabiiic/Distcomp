using Lab1.Core.Contracts;
using Lab1.Core.Models;
namespace Lab1.Core.Abstractions
{
    public interface IStickerRepository
    {
        StickerResponseTo? Get(ulong id);
        StickerResponseTo Create(Sticker sticker);
        List<StickerResponseTo> GetAll();
        bool Delete(ulong id);
        StickerResponseTo? Update(Sticker sticker, ulong id);
    }
}
