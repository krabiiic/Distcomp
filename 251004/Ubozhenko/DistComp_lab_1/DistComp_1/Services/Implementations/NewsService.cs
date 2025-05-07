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

public class NewsService : INewsService
{
    private readonly INewsRepository _newsRepository;
    private readonly IMapper _mapper;
    private readonly NewsRequestDTOValidator _validator;

    public NewsService(INewsRepository newsRepository,
        IMapper mapper, NewsRequestDTOValidator validator)
    {
        _newsRepository = newsRepository;
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
        var newsItem = await _newsRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        return _mapper.Map<NewsResponseDTO>(newsItem);
    }

    public async Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToCreate = _mapper.Map<News>(news);
        
        newsToCreate.Created = DateTime.Now;
        newsToCreate.Modified = DateTime.Now;
        
        var createdNews = await _newsRepository.CreateAsync(newsToCreate);
        return _mapper.Map<NewsResponseDTO>(createdNews);
    }

    public async Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var newsToUpdate = _mapper.Map<News>(news);
        
        newsToUpdate.Modified = DateTime.Now;
        
        var updatedNews = await _newsRepository.UpdateAsync(newsToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(news.Id));
        return _mapper.Map<NewsResponseDTO>(updatedNews);
    }

    public async Task DeleteNewsAsync(long id)
    {
        if (await _newsRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        }
    }
}
