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

public class NoteService : INoteService
{
    private readonly INoteRepository _noteRepository;
    private readonly IMapper _mapper;
    private readonly NoteRequestDTOValidator _validator;
    
    public NoteService(INoteRepository noteRepository, 
        IMapper mapper, NoteRequestDTOValidator validator)
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
        if (await _noteRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.NoteNotFound, ErrorMessages.NoteNotFoundMessage(id));
        }
    }
}
