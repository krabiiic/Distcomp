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

public class NewsService : INewsService
{
    private readonly INewsRepository _newsRepository;
    private readonly IAuthorRepository _authorRepository;
    private readonly IMapper _mapper;
    private readonly NewsRequestDTOValidator _validator;

    public NewsService(INewsRepository newsRepository, IAuthorRepository authorRepository,
        IMapper mapper, NewsRequestDTOValidator validator)
    {
        _newsRepository = newsRepository;
        _authorRepository = authorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<NewsResponseDTO>> GetNewsAsync()
    {
        var news = await _newsRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<NewsResponseDTO>>(news);
    }

    public async Task<NewsResponseDTO> GetNewsByIdAsync(long id)
    {
        var news = await _newsRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        return _mapper.Map<NewsResponseDTO>(news);
    }

    public async Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToCreate = _mapper.Map<News>(news);

        newsToCreate.AuthorId = news.AuthorId;
        newsToCreate.Created = DateTime.UtcNow;
        newsToCreate.Modified = DateTime.UtcNow;
        
        var createdNews = await _newsRepository.CreateAsync(newsToCreate);
        return _mapper.Map<NewsResponseDTO>(createdNews);
    }

    public async Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToUpdate = _mapper.Map<News>(news);
        
        newsToUpdate.Modified = DateTime.UtcNow;
        
        var updatedNews = await _newsRepository.UpdateAsync(newsToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(news.Id));
        return _mapper.Map<NewsResponseDTO>(updatedNews);
    }

    public async Task DeleteNewsAsync(long id)
    {
        if (!await _newsRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        }
    }
}