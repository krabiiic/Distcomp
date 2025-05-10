using AutoMapper;
using Discussion.DTO.Request;
using Discussion.DTO.Response;
using Discussion.Exceptions;
using Discussion.Infrastructure.Validators;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using Discussion.Services.Interfaces;
using FluentValidation;

namespace Discussion.Services.Implementations;

public class ReactionService : IReactionService
{
    private readonly IReactionRepository _reactionRepository;
    private readonly IMapper _mapper;
    private readonly ReactionRequestDtoValidator _validator;
    
    public ReactionService(IReactionRepository reactionRepository, 
        IMapper mapper, ReactionRequestDtoValidator validator)
    {
        _reactionRepository = reactionRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<ReactionResponseDTO>> GetReactionsAsync()
    {
        var reactions = await _reactionRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<ReactionResponseDTO>>(reactions);
    }

    public async Task<ReactionResponseDTO> GetReactionByIdAsync(long id)
    {
        var reaction = await _reactionRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.ReactionNotFound, ErrorMessages.ReactionNotFoundMessage(id));
        return _mapper.Map<ReactionResponseDTO>(reaction);
    }

    public async Task<ReactionResponseDTO> CreateReactionAsync(ReactionRequestDTO reaction)
    {
        await _validator.ValidateAndThrowAsync(reaction);
        var reactionToCreate = _mapper.Map<Reaction>(reaction);
        var createdReaction = await _reactionRepository.CreateAsync(reactionToCreate);
        return _mapper.Map<ReactionResponseDTO>(createdReaction);
    }

    public async Task<ReactionResponseDTO> UpdateReactionAsync(ReactionRequestDTO reaction)
    {
        await _validator.ValidateAndThrowAsync(reaction);
        var reactionToUpdate = _mapper.Map<Reaction>(reaction);
        var updatedReaction = await _reactionRepository.UpdateAsync(reactionToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.ReactionNotFound, ErrorMessages.ReactionNotFoundMessage(reaction.Id));
        return _mapper.Map<ReactionResponseDTO>(updatedReaction);
    }

    public async Task DeleteReactionAsync(long id)
    {
        if (!await _reactionRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.ReactionNotFound, ErrorMessages.ReactionNotFoundMessage(id));
        }
    }
}
