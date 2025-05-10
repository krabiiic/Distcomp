using Lab1.Core.Contracts;
using Lab1.Core.Models;
using System.Numerics;

namespace Lab1.Core.Abstractions
{
    public interface ICreatorService
    {
        CreatorResponseTo? GetCreator(ulong id);
        CreatorResponseTo CreateCreator(Creator creator);
        bool DeleteCreator(ulong id);
        List<CreatorResponseTo> GetAllCreators();
        CreatorResponseTo? UpdateCreator(Creator creator, ulong id);
    }
}
