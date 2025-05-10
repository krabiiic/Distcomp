using Microsoft.EntityFrameworkCore;
using System.Linq.Expressions;
using WebApplication1.DTO;
using WebApplication1.Entity;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public class ArticleService : IArticleService
    {
        private readonly IRepository<Article> _articleRepo;
        private readonly IRepository<User> _userRepo;
        private readonly IRepository<Sticker> _stickerRepo; 

        public ArticleService(IRepository<Article> articleRepo, IRepository<User> userRepo, IRepository<Sticker> stickerRepo)
        {
            _articleRepo = articleRepo;
            _userRepo = userRepo;
            _stickerRepo = stickerRepo;
        }

        public async Task<ArticleResponseTo> CreateArticleAsync(ArticleRequestTo dto)
        {
            if (string.IsNullOrWhiteSpace(dto.Title))
            {
                throw new ValidationException("Title cannot be empty", 400, "40002");
            }
            if (dto.Title.Length < 3)
            {
                throw new ValidationException("Title must be at least 3 characters", 400, "40012");
            }
            if (dto.Title.Length > 64)
            {
                throw new ValidationException("Title is too long", 400, "40014");
            }

            bool duplicateExists;
            try
            {
                duplicateExists = await _articleRepo.ExistsAsync(a =>
                    EF.Functions.ILike(a.Title, dto.Title));
            }
            catch (Exception)
            {
                throw new ValidationException("Error checking title uniqueness", 500, "50001");
            }

            if (duplicateExists)
            {
                throw new ValidationException($"Article with title '{dto.Title}' already exists", 403, "40303");
            }

            User? user;
            try
            {
                user = await _userRepo.GetByIdAsync(dto.UserId);
            }
            catch (Exception)
            {
                throw new ValidationException("Error checking user existence", 500, "50002");
            }

            if (user == null)
            {
                throw new ValidationException("User not found", 404, "40401");
            }

            try
            {
                var articleEntity = new Article
                {
                    UserId = dto.UserId,
                    Title = dto.Title,
                    Content = dto.Content,
                    Created = DateTime.UtcNow,
                    Modified = DateTime.UtcNow
                };

                var created = await _articleRepo.CreateAsync(articleEntity);

                bool autoStickersExist = await _stickerRepo.ExistsAsync(s => s.Name == $"red{dto.UserId}");
                if (!autoStickersExist)
                {
                    string[] colors = { "red", "green", "blue" };
                    foreach (var color in colors)
                    {
                        var sticker = new Sticker
                        {
                            Name = $"{color}{dto.UserId}"
                        };
                        var createdSticker = await _stickerRepo.CreateAsync(sticker);
                        created.Stickers.Add(createdSticker);
                    }
                    await _articleRepo.UpdateAsync(created);
                }

                return MapToResponse(created);
            }
            catch (Exception)
            {
                throw new ValidationException("Error creating article", 500, "50003");
            }
        }

        private ArticleResponseTo MapToResponse(Article article)
        {
            return new ArticleResponseTo
            {
                Id = article.Id,
                UserId = article.UserId,
                Title = article.Title,
                Content = article.Content,
                Created = article.Created,
                Modified = article.Modified
            };
        }

        public async Task<ArticleResponseTo> GetArticleByIdAsync(long id)
        {
            var article = await _articleRepo.GetByIdAsync(id);
            if (article == null)
            {
                throw new ValidationException($"Article with id {id} not found", 404, "40403");
            }
            return MapToResponse(article);
        }

        public async Task<PaginatedResult<ArticleResponseTo>> GetAllArticlesAsync(
            int pageNumber = 1,
            int pageSize = 10,
            string? sortBy = null,
            string? filter = null)
        {
            Expression<Func<Article, bool>>? filterExp = null;
            if (!string.IsNullOrWhiteSpace(filter))
            {
                filterExp = a => a.Title.Contains(filter);
            }

            Func<IQueryable<Article>, IOrderedQueryable<Article>> orderBy = sortBy switch
            {
                "title_asc" => q => q.OrderBy(a => a.Title),
                "title_desc" => q => q.OrderByDescending(a => a.Title),
                _ => q => q.OrderByDescending(a => a.Created)
            };

            var pagedArticles = await _articleRepo.GetAllAsync(pageNumber, pageSize, filterExp, orderBy);
            var resultDto = new PaginatedResult<ArticleResponseTo>(
                pagedArticles.Items.Select(a => new ArticleResponseTo
                {
                    Id = a.Id,
                    UserId = a.UserId,
                    Title = a.Title,
                    Content = a.Content,
                    Created = a.Created,
                    Modified = a.Modified
                }),
                pagedArticles.TotalCount,
                pagedArticles.PageNumber,
                pagedArticles.PageSize);
            return resultDto;
        }
        public async Task<ArticleResponseTo> UpdateArticleAsync(long id, ArticleRequestTo dto)
        {
            var existing = await _articleRepo.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ValidationException($"Article with id {id} not found", 404, "40406");
            }
            if (string.IsNullOrWhiteSpace(dto.Title) || dto.Title.Length < 3)
            {
                throw new ValidationException("Title must be at least 3 characters", 400, "40012");
            }
            if (dto.Title.Length > 64)
            {
                throw new ValidationException("Title is too long", 400, "40014");
            }

            var user = await _userRepo.GetByIdAsync(dto.UserId);
            if (user == null)
            {
                throw new ValidationException("User not found", 404, "40401");
            }

            if (!existing.Title.Equals(dto.Title, StringComparison.OrdinalIgnoreCase))
            {
                bool duplicateExists = await _articleRepo.ExistsAsync(a =>
                    a.Title.ToLower() == dto.Title.ToLower() && a.Id != id);
                if (duplicateExists)
                {
                    throw new ValidationException($"Article with title '{dto.Title}' already exists", 403, "40303");
                }
            }

            existing.UserId = dto.UserId;
            existing.Title = dto.Title;
            existing.Content = dto.Content;
            existing.Modified = DateTime.UtcNow;

            await _articleRepo.UpdateAsync(existing);

            var updatedEntity = await _articleRepo.GetByIdAsync(id);
            return new ArticleResponseTo
            {
                Id = updatedEntity.Id,
                UserId = updatedEntity.UserId,
                Title = updatedEntity.Title,
                Content = updatedEntity.Content,
                Created = updatedEntity.Created,
                Modified = updatedEntity.Modified
            };
        }

        public async Task DeleteArticleAsync(long id)
        {
            var article = await _articleRepo.GetByIdAsync(id);
            if (article == null)
                throw new ValidationException($"Article with id {id} not found", 404, "40408");

            var userId = article.UserId;
            await _articleRepo.DeleteAsync(id);

            bool userHasOtherArticles = await _articleRepo.ExistsAsync(a => a.UserId == userId);
            if (!userHasOtherArticles)
            {
                string[] colors = { "red", "green", "blue" };
                foreach (var color in colors)
                {
                    string stickerName = $"{color}{userId}";
                    var autoStickers = await _stickerRepo.GetAllAsync(1, 10, s => s.Name == stickerName, null);
                    foreach (var sticker in autoStickers.Items)
                    {
                        await _stickerRepo.DeleteAsync(sticker.Id);
                    }
                }
            }
        }

    }
}
