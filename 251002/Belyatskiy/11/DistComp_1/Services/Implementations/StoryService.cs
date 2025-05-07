using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;

namespace DistComp_1.Services.Implementations;

public class ArticleService : IArticleService
{
    private readonly IArticleRepository _ArticleRepository;
    private readonly IMapper _mapper;
    private readonly ArticleRequestDTOValidator _validator;

    public ArticleService(IArticleRepository ArticleRepository,
        IMapper mapper, ArticleRequestDTOValidator validator)
    {
        _ArticleRepository = ArticleRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<ArticleResponseDTO>> GetStoriesAsync()
    {
        var stories = await _ArticleRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<ArticleResponseDTO>>(stories);
    }

    public async Task<ArticleResponseDTO> GetArticleByIdAsync(long id)
    {
        var Article = await _ArticleRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(id));
        return _mapper.Map<ArticleResponseDTO>(Article);
    }

    public async Task<ArticleResponseDTO> CreateArticleAsync(ArticleRequestDTO Article)
    {
        await _validator.ValidateAndThrowAsync(Article);
        var ArticleToCreate = _mapper.Map<Article>(Article);
        
        ArticleToCreate.Created = DateTime.Now;
        ArticleToCreate.Modified = DateTime.Now;
        
        var createdArticle = await _ArticleRepository.CreateAsync(ArticleToCreate);
        return _mapper.Map<ArticleResponseDTO>(createdArticle);
    }

    public async Task<ArticleResponseDTO> UpdateArticleAsync(ArticleRequestDTO Article)
    {
        await _validator.ValidateAndThrowAsync(Article);
        var ArticleToUpdate = _mapper.Map<Article>(Article);
        
        ArticleToUpdate.Modified = DateTime.Now;
        
        var updatedArticle = await _ArticleRepository.UpdateAsync(ArticleToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(Article.Id));
        return _mapper.Map<ArticleResponseDTO>(updatedArticle);
    }

    public async Task DeleteArticleAsync(long id)
    {
        if (await _ArticleRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(id));
        }
    }
}