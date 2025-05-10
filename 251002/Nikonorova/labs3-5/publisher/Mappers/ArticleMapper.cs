using publisher.DTO.Request;
using publisher.DTO.Response;
using Riok.Mapperly.Abstractions;
using publisher.Models;
using static System.Net.Mime.MediaTypeNames;

namespace publisher.Mappers
{
    [Mapper]
    public static partial class ArticleMapper
    {
        public static partial Article ToEntity(this CreateArticleRequestTo createArticleRequestTo);
        public static partial ArticleResponseTo ToResponse(this Article article);
        public static partial IEnumerable<ArticleResponseTo> ToResponse(this IEnumerable<Article> articles);
    }
}
