using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories;
using Publisher.Repositories.Implementations;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Publisher.Services.Implementations;

public class EditorService : IEditorService
{
    private readonly IEditorRepository _editorRepository;
    private readonly IMapper _mapper;
    private readonly EditorRequestDTOValidator _validator;
    
    public EditorService(IEditorRepository editorRepository, 
        IMapper mapper, EditorRequestDTOValidator validator)
    {
        _editorRepository = editorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<EditorResponseDTO>> GetEditorsAsync()
    {
        var editors = await _editorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<EditorResponseDTO>>(editors);
    }

    public async Task<EditorResponseDTO> GetEditorByIdAsync(long id)
    {
        var editor = await _editorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.EditorNotFound, ErrorMessages.EditorNotFoundMessage(id));
        return _mapper.Map<EditorResponseDTO>(editor);
    }

    public async Task<EditorResponseDTO> CreateEditorAsync(EditorRequestDTO editor)
    {
        await _validator.ValidateAndThrowAsync(editor);
        var editorToCreate = _mapper.Map<Editor>(editor);
        var createdEditor = await _editorRepository.CreateAsync(editorToCreate);
        return _mapper.Map<EditorResponseDTO>(createdEditor);
    }

    public async Task<EditorResponseDTO> UpdateEditorAsync(EditorRequestDTO editor)
    {
        await _validator.ValidateAndThrowAsync(editor);
        var editorToUpdate = _mapper.Map<Editor>(editor);
        var updatedEditor = await _editorRepository.UpdateAsync(editorToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.EditorNotFound, ErrorMessages.EditorNotFoundMessage(editor.Id));
        return _mapper.Map<EditorResponseDTO>(updatedEditor);
    }

    public async Task DeleteEditorAsync(long id)
    {
        if (!await _editorRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.EditorNotFound, ErrorMessages.EditorNotFoundMessage(id));
        }
    }
}