using System.Numerics;
namespace Lab1.Core.Abstractions
{
    public interface IEntity
    {
        // Контракт на наличие ID у соответствующих сущностей
        ulong Id { get; set; }
    }
}
