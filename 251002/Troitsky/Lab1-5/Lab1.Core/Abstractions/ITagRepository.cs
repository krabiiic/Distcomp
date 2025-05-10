using Lab1.Core.Contracts;
using Lab1.Core.Models;
namespace Lab1.Core.Abstractions
{
    public interface ITagRepository
    {
        TagResponseTo? Get(ulong id);
        TagResponseTo Create(Tag tag);
        List<TagResponseTo> GetAll();
        bool Delete(ulong id);
        TagResponseTo? Update(Tag tag, ulong id);
    }
}
