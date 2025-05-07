using Core.DTO;
using Core.Interfaces;
using AutoMapper;
using Core.Entities;
using Core.Exceptions;

namespace Core.Services;

public class CreatorService : ICreatorService
{
    private readonly ICreatorRepository _creatorRepository;
    private readonly IArticleRepository _articleRepository;
    private readonly IMapper _mapper;

    public CreatorService(ICreatorRepository creatorRepository,IArticleRepository articleRepository, IMapper mapper)
    {
        _creatorRepository = creatorRepository;
        _articleRepository = articleRepository;
        _mapper = mapper;
    }
    
    public async Task<CreatorResponseToGetById?> CreateCreator(CreatorRequestToCreate model)
    {
        Creator creator = _mapper.Map<Creator>(model);
        Validate(creator);
        creator = await _creatorRepository.AddCreator(creator);
        return _mapper.Map<CreatorResponseToGetById?>(creator);
    }

    public async Task<IEnumerable<CreatorResponseToGetById?>?> GetCreators(CreatorRequestToGetAll request)
    {
        var creators = await _creatorRepository.GetAllCreators();
        return creators.Select(_mapper.Map<CreatorResponseToGetById?>);
    }

    public async Task<CreatorResponseToGetById?> GetCreatorById(CreatorRequestToGetById request)
    {
        var creator = await _creatorRepository.GetCreator(request.Id);
        return _mapper.Map<CreatorResponseToGetById>(creator);
    }
    public async Task<CreatorResponseToGetById?> GetCreatorByArticleId(CreatorRequestToGetByArticleId request)
    {
        var article = await _articleRepository.GetArticle(request.TagId);
        var creator = await _creatorRepository.GetCreator(article.CreatorId);
        return _mapper.Map<CreatorResponseToGetById>(creator);
    }

    public async Task<CreatorResponseToGetById?> UpdateCreator(CreatorRequestToFullUpdate model)
    {
        var creator = _mapper.Map<Creator>(model);
        Validate(creator);
        var creatorProj = await _creatorRepository.UpdateCreator(creator);
        return _mapper.Map<CreatorResponseToGetById>(creatorProj);
    }

    public async Task<CreatorResponseToGetById?> DeleteCreator(CreatorRequestToDeleteById request)
    {
        var creator = await _creatorRepository.RemoveCreator(request.Id);
        return _mapper.Map<CreatorResponseToGetById>(creator);
    }
    
    private bool Validate(Creator creator)
    {
        var errors = new Dictionary<string, string[]>();
        if (creator.Firstname.Length < 2 || creator.Firstname.Length > 64)
        {
            errors.Add("Firstname",["Should be from 2 to 64 chars"]);
        }
        if (creator.Lastname.Length < 2 || creator.Lastname.Length > 64)
        {
            errors.Add("Lastname",["Should be from 2 to 64 chars"]);
        }
        if (creator.Password.Length < 8 || creator.Firstname.Length > 128)
        {
            errors.Add("Password",["Should be from 8 to 128 chars"]);
        }
        if (creator.Login.Length < 2 || creator.Login.Length > 64)
        {
            errors.Add("Login",["Should be from 2 to 64 chars"]);
        }

        if (errors.Count != 0)
        {
            throw new BadRequestException("Validation error", errors);
        }
        return true;
    }
}