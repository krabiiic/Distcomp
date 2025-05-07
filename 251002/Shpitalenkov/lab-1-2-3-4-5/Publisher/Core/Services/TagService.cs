using Core.DTO;
using Core.Interfaces;
using AutoMapper;
using Core.Entities;
using Core.Exceptions;

namespace Core.Services;

public class TagService (ITagRepository _tagRepository, IMapper _mapper) : ITagService
{
    public async Task<TagResponseToGetById?> CreateTag(TagRequestToCreate model)
    {
        Tag tag = _mapper.Map<Tag>(model);
        Validate(tag);
        tag = await _tagRepository.AddTag(tag);
        return _mapper.Map<TagResponseToGetById>(tag);
    }

    public async Task<IEnumerable<TagResponseToGetById?>?> GetTags(TagRequestToGetAll request)
    {
        var tag = await _tagRepository.GetAllTag();
        return tag.Select(_mapper.Map<TagResponseToGetById>);
    }
    
    public async Task<IEnumerable<TagResponseToGetById>?> CreateTagsIfDontExist(IEnumerable<string> request)
    {
        var tag = await _tagRepository.GetTagsCreateIfNotExist(request);
        return tag?.Select(_mapper.Map<TagResponseToGetById>);
    }

    public async Task<TagResponseToGetById?> GetTagById(TagRequestToGetById request)
    {
        var tag = await _tagRepository.GetTag(request.Id);
        return _mapper.Map<TagResponseToGetById>(tag);
    }

    public async Task<TagResponseToGetById?> UpdateTag(TagRequestToFullUpdate model)
    {
        var tag = _mapper.Map<Tag>(model);
        Validate(tag);
        tag = await _tagRepository.UpdateTag(tag);
        return _mapper.Map<TagResponseToGetById>(tag);
    }

    public async Task<TagResponseToGetById?> DeleteTag(TagRequestToDeleteById request)
    {
        var tag = await _tagRepository.RemoveTag(request.Id);
        return _mapper.Map<TagResponseToGetById>(tag);
    }
    
    private bool Validate(Tag tag)
    {
        var errors = new Dictionary<string, string[]>();
        if (tag.Name.Length < 2 || tag.Name.Length > 32)
        {
            errors.Add("Name",["Should be from 2 to 32 chars"]);
        }
        if (errors.Count != 0)
        {
            throw new BadRequestException("Validation error", errors);
        }
        return true;
    }
}