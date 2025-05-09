using Lab1.Core.Contracts;
using Lab1.Core.Models;

namespace Lab1.Core.Abstractions
{
    public interface INewsService
    {
        NewsResponseTo? GetNews(ulong id);
        NewsResponseTo CreateNews(News news);
        bool DeleteNews(ulong id);
        List<NewsResponseTo> GetAllNews();
        NewsResponseTo? UpdateNews(News news, ulong id);
    }
}
