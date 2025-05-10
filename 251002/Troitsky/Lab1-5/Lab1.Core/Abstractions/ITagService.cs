using Lab1.Core.Contracts;
using Lab1.Core.Models;

namespace Lab1.Core.Abstractions
{
    public interface ITagService
    {
        TagResponseTo? GetTag(ulong id);
        TagResponseTo CreateTag(Tag tag);
        bool DeleteTag(ulong id);
        List<TagResponseTo> GetAllTags();
        TagResponseTo? UpdateTag(Tag tag, ulong id);
    }
}
