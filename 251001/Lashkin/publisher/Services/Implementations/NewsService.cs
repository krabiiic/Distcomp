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
    private readonly IUserRepository _userRepository;
    private readonly IMapper _mapper;
    private readonly NewsRequestDTOValidator _validator;

    public NewsService(INewsRepository newsRepository, IUserRepository userRepository,
        IMapper mapper, NewsRequestDTOValidator validator)
    {
        _newsRepository = newsRepository;
        _userRepository = userRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<NewsResponseDTO>> GetNewsAsync()
    {
        var stories = await _newsRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<NewsResponseDTO>>(stories);
    }

    public async Task<NewsResponseDTO> GetNewsByIdAsync(long id)
    {
        var story = await _newsRepository.GetByIdAsync(id)
                    ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        return _mapper.Map<NewsResponseDTO>(story);
    }

    public async Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var storyToCreate = _mapper.Map<News>(news);

        storyToCreate.UserId = news.UserId;
        storyToCreate.Created = DateTime.UtcNow;
        storyToCreate.Modified = DateTime.UtcNow;
        
        var createdStory = await _newsRepository.CreateAsync(storyToCreate);
        return _mapper.Map<NewsResponseDTO>(createdStory);
    }

    public async Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news)
    {
        await _validator.ValidateAndThrowAsync(news);
        var storyToUpdate = _mapper.Map<News>(news);
        
        storyToUpdate.Modified = DateTime.UtcNow;
        storyToUpdate.Created = DateTime.UtcNow;
        
        var updatedStory = await _newsRepository.UpdateAsync(storyToUpdate)
                           ?? throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(news.Id));
        return _mapper.Map<NewsResponseDTO>(updatedStory);
    }

    public async Task DeleteNewsAsync(long id)
    {
        if (!await _newsRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.NewsNotFound, ErrorMessages.NewsNotFoundMessage(id));
        }
    }
}