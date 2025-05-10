using System.Linq.Expressions;
using WebApplication1.DTO;
using WebApplication1.Entity;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public class StickerService : IStickerService
    {
        private readonly IRepository<Sticker> _stickerRepo;

        public StickerService(IRepository<Sticker> stickerRepo)
        {
            _stickerRepo = stickerRepo;
        }
        public async Task<StickerResponseTo> CreateStickerAsync(StickerRequestTo dto)
        {
            if (string.IsNullOrWhiteSpace(dto.Name))
            {
                throw new ValidationException("Sticker name cannot be empty", 400, "40005");
            }
            if (dto.Name.Length < 3)
            {
                throw new ValidationException("Sticker name must be at least 3 characters", 400, "40006");
            }
            if (dto.Name.Length > 20)
            {
                throw new ValidationException("Sticker name is too long", 400, "40007");
            }

            var sticker = new Sticker
            {
                Name = dto.Name
            };

            var created = await _stickerRepo.CreateAsync(sticker);

            var digits = new string(dto.Name.Where(char.IsDigit).ToArray());
            if (!string.IsNullOrEmpty(digits))
            {
                string[] colors = { "red", "green", "blue" };
                foreach (var color in colors)
                {
                    var autoSticker = new Sticker
                    {
                        Name = $"{color}{digits}"
                    };
                    await _stickerRepo.CreateAsync(autoSticker);
                }
            }

            return new StickerResponseTo
            {
                Id = created.Id,
                Name = created.Name
            };
        }


        public async Task DeleteStickerAsync(long id)
        {
            var existing = await _stickerRepo.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ValidationException($"Sticker with id {id} not found", 404, "40408");
            }
            await _stickerRepo.DeleteAsync(id);
        }
        public async Task<PaginatedResult<StickerResponseTo>> GetAllStickersAsync(
            int pageNumber = 1,
            int pageSize = 10)
        {
            Expression<Func<Sticker, bool>> filter = s =>
                !s.Name.StartsWith("red") &&
                !s.Name.StartsWith("green") &&
                !s.Name.StartsWith("blue");

            var pagedStickers = await _stickerRepo.GetAllAsync(
                pageNumber,
                pageSize,
                filter,
                orderBy: q => q.OrderByDescending(s => s.Id)
            );

            var resultDto = new PaginatedResult<StickerResponseTo>(
                pagedStickers.Items.Select(s => new StickerResponseTo
                {
                    Id = s.Id,
                    Name = s.Name
                }),
                pagedStickers.TotalCount,
                pagedStickers.PageNumber,
                pagedStickers.PageSize);
            return resultDto;
        }


        public async Task<StickerResponseTo> GetStickerByIdAsync(long id)
        {
            var sticker = await _stickerRepo.GetByIdAsync(id);
            if (sticker == null)
            {
                throw new ValidationException($"Sticker with id {id} not found", 404, "40406");
            }
            return new StickerResponseTo
            {
                Id = sticker.Id,
                Name = sticker.Name
            };
        }
        public async Task<StickerResponseTo> UpdateStickerAsync(long id, StickerRequestTo dto)
        {
            var existing = await _stickerRepo.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ValidationException($"Sticker with id {id} not found", 404, "40407");
            }
            if (string.IsNullOrWhiteSpace(dto.Name))
            {
                throw new ValidationException("Sticker name cannot be empty", 400, "40005");
            }
            if (dto.Name.Length < 3)
            {
                throw new ValidationException("Sticker name must be at least 3 characters", 400, "40006");
            }
            if (dto.Name.Length > 20)
            {
                throw new ValidationException("Sticker name is too long", 400, "40007");
            }

            existing.Name = dto.Name;
            await _stickerRepo.UpdateAsync(existing);
            var updatedEntity = await _stickerRepo.GetByIdAsync(id);
            return new StickerResponseTo
            {
                Id = updatedEntity.Id,
                Name = updatedEntity.Name
            };
        }

    }
}
