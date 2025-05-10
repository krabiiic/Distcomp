using Lab1.Core.Contracts;
using Lab1.Core.Models;
using System.Numerics;
namespace Lab1.Core.Abstractions
{
    public interface ICreatorRepository
    {
        CreatorResponseTo? Get(ulong id);
        CreatorResponseTo Create(Creator creator);
        List<CreatorResponseTo> GetAll();
        bool Delete(ulong id);
        CreatorResponseTo? Update(Creator creator, ulong id);
    }
}
