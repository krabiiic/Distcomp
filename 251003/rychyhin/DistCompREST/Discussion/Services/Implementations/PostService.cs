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

public class NoteService : INoteService
{
    private readonly INoteRepository _noteRepository;
    private readonly IMapper _mapper;
    private readonly NoteRequestDtoValidator _validator;
    
    public NoteService(INoteRepository noteRepository, 
        IMapper mapper, NoteRequestDtoValidator validator)
    {
        _noteRepository = noteRepository;
        _mapper = mapper;
        _validator = validator;
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
        var createdNote = await _noteRepository.CreateAsync(noteToCreate);
        return _mapper.Map<NoteResponseDTO>(createdNote);
    }

    public async Task<NoteResponseDTO> UpdateNoteAsync(long id, NoteRequestDTO note)
    {
        await _validator.ValidateAndThrowAsync(note);
        var noteToUpdate = _mapper.Map<Note>(note);
        var updatedNote = await _noteRepository.UpdateAsync(noteToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(id));
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
