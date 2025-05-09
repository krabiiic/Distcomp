using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Exceptions;
using DistComp.Infrastructure.Validators;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using DistComp.Services.Interfaces;
using FluentValidation;

namespace DistComp.Services.Implementations;

public class NoteService : INoteService
{
    private readonly INoteRepository _noteRepository;
    private readonly IMapper _mapper;
    private readonly NoteRequestDTOValidator _validator;
    private readonly IIssueRepository _issueRepository;
    
    public NoteService(INoteRepository noteRepository, 
        IMapper mapper, NoteRequestDTOValidator validator, IIssueRepository issueRepository)
    {
        _noteRepository = noteRepository;
        _mapper = mapper;
        _validator = validator;
        _issueRepository = issueRepository;
    }
    
    public async Task<IEnumerable<NoteResponseDTO>> GetNotesAsync()
    {
        var notes = await _noteRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<NoteResponseDTO>>(notes);
    }

    public async Task<NoteResponseDTO> GetNoteByIdAsync(long id)
    {
        var note = await _noteRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(id));
        return _mapper.Map<NoteResponseDTO>(note);
    }

    public async Task<NoteResponseDTO> CreateNoteAsync(NoteRequestDTO note)
    {
        await _validator.ValidateAndThrowAsync(note);
        var noteToCreate = _mapper.Map<Note>(note);
        if (!await _issueRepository.HasIssue(noteToCreate.IssueId))
        {
            throw new ConflictException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(noteToCreate.Id));
        }
        var createdNote = await _noteRepository.CreateAsync(noteToCreate);
        return _mapper.Map<NoteResponseDTO>(createdNote);
    }

    public async Task<NoteResponseDTO> UpdateNoteAsync(NoteRequestDTO note)
    {
        await _validator.ValidateAndThrowAsync(note);
        var noteToUpdate = _mapper.Map<Note>(note);
        var updatedNote = await _noteRepository.UpdateAsync(noteToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(note.Id));
        return _mapper.Map<NoteResponseDTO>(updatedNote);
    }

    public async Task DeleteNoteAsync(long id)
    {
        if (!await _noteRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(id));
        }
    }
}
