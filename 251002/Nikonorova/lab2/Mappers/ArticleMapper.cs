using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using Riok.Mapperly.Abstractions;
using lab2_jpa.Models;

namespace lab2_jpa.Mappers
{
    [Mapper]
    public static partial class ArticleMapper
    {
        public static partial Article Map(UpdateArticleRequestTo updateArticleRequestTo);
        public static partial Article Map(CreateArticleRequestTo createArticleRequestTo);
        public static partial ArticleResponseTo Map(Article article);
        public static partial IEnumerable<ArticleResponseTo> Map(IEnumerable<Article> articles);

        public static partial IEnumerable<Article> Map(
            IEnumerable<UpdateArticleRequestTo> articleRequestTos);
    }
}
