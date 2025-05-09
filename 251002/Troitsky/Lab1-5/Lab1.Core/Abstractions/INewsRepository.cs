using Lab1.Core.Contracts;
using Lab1.Core.Models;
namespace Lab1.Core.Abstractions
{
    public interface INewsRepository
    {
        NewsResponseTo? Get(ulong id);
        NewsResponseTo Create(News news);
        List<NewsResponseTo> GetAll();
        bool Delete(ulong id);
        NewsResponseTo? Update(News news, ulong id);
    }
}
