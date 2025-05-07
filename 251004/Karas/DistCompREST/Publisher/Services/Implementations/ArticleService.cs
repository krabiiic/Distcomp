using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class ArticleService : IArticleService
{
    private readonly IArticleRepository _articleRepository;
    private readonly IEditorRepository _editorRepository;
    private readonly IMapper _mapper;
    private readonly ArticleRequestDTOValidator _validator;

    public ArticleService(IArticleRepository articleRepository, IEditorRepository editorRepository,
        IMapper mapper, ArticleRequestDTOValidator validator)
    {
        _articleRepository = articleRepository;
        _editorRepository = editorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<ArticleResponseDTO>> GetStoriesAsync()
    {
        var stories = await _articleRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<ArticleResponseDTO>>(stories);
    }

    public async Task<ArticleResponseDTO> GetArticleByIdAsync(long id)
    {
        var article = await _articleRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(id));
        return _mapper.Map<ArticleResponseDTO>(article);
    }

    public async Task<ArticleResponseDTO> CreateArticleAsync(ArticleRequestDTO article)
    {
        await _validator.ValidateAndThrowAsync(article);
        var articleToCreate = _mapper.Map<Article>(article);

        articleToCreate.EditorId = article.EditorId;
        articleToCreate.Created = DateTime.UtcNow;
        articleToCreate.Modified = DateTime.UtcNow;
        
        var createdArticle = await _articleRepository.CreateAsync(articleToCreate);
        return _mapper.Map<ArticleResponseDTO>(createdArticle);
    }

    public async Task<ArticleResponseDTO> UpdateArticleAsync(ArticleRequestDTO article)
    {
        await _validator.ValidateAndThrowAsync(article);
        var articleToUpdate = _mapper.Map<Article>(article);
        
        articleToUpdate.Modified = DateTime.UtcNow;
        articleToUpdate.Created = DateTime.UtcNow;
        
        var updatedArticle = await _articleRepository.UpdateAsync(articleToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(article.Id));
        return _mapper.Map<ArticleResponseDTO>(updatedArticle);
    }

    public async Task DeleteArticleAsync(long id)
    {
        if (!await _articleRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.ArticleNotFound, ErrorMessages.ArticleNotFoundMessage(id));
        }
    }
}