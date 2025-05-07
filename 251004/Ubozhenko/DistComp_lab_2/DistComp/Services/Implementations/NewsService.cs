using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Exceptions;
using DistComp.Infrastructure.Validators;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using DistComp.Services.Interfaces;
using FluentValidation;
using NewsRequestDTO = DistComp.DTO.ResponseDTO.NewsRequestDTO;

namespace DistComp.Services.Implementations;

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
    
    public async Task<IEnumerable<NewsRequestDTO>> GetNewsAsync()
    {
        var news = await _newsRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<NewsRequestDTO>>(news);
    }

    public async Task<NewsRequestDTO> GetNewsByIdAsync(long id)
    {
        var news = await _newsRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        return _mapper.Map<NewsRequestDTO>(news);
    }

    public async Task<NewsRequestDTO> CreateNewsAsync(DTO.RequestDTO.NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToCreate = _mapper.Map<News>(news);

        newsToCreate.AuthorId = news.AuthorId;
        newsToCreate.Created = DateTime.UtcNow;
        newsToCreate.Modified = DateTime.UtcNow;
        
        var createdNews = await _newsRepository.CreateAsync(newsToCreate);
        return _mapper.Map<NewsRequestDTO>(createdNews);
    }

    public async Task<NewsRequestDTO> UpdateNewsAsync(DTO.RequestDTO.NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToUpdate = _mapper.Map<News>(news);
        
        newsToUpdate.Modified = DateTime.UtcNow;
        
        var updatedNews = await _newsRepository.UpdateAsync(newsToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(news.Id));
        return _mapper.Map<NewsRequestDTO>(updatedNews);
    }

    public async Task DeleteNewsAsync(long id)
    {
        if (!await _newsRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        }
    }
}