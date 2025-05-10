package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.requests.EditorRequestDto;
import by.bsuir.publisher.dto.requests.converters.EditorRequestConverter;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import by.bsuir.publisher.dto.responses.converters.EditorResponseConverter;
import by.bsuir.publisher.dto.responses.converters.CollectionEditorResponseConverter;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.EditorRepository;
import by.bsuir.publisher.services.EditorService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class EditorServiceImpl implements EditorService {

    private final EditorRepository editorRepository;
    private final EditorRequestConverter editorRequestConverter;
    private final EditorResponseConverter editorResponseConverter;
    private final CollectionEditorResponseConverter collectionEditorResponseConverter;

    @Override
    @Validated
    public EditorResponseDto create(@Valid @NonNull EditorRequestDto dto) throws EntityExistsException {
        Optional<Editor> editor = dto.getId() == null ? Optional.empty() : editorRepository.findById(dto.getId());
        if (editor.isEmpty()) {
            return editorResponseConverter.toDto(editorRepository.save(editorRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<EditorResponseDto> read(@NonNull Long id) {
        return editorRepository.findById(id).flatMap(editor -> Optional.of(
                editorResponseConverter.toDto(editor)));
    }

    @Override
    @Validated
    public EditorResponseDto update(@Valid @NonNull EditorRequestDto dto) throws NoEntityExistsException {
        Optional<Editor> editor = dto.getId() == null || editorRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(editorRequestConverter.fromDto(dto));
        return editorResponseConverter.toDto(editorRepository.save(editor.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Editor> editor = editorRepository.findById(id);
        editorRepository.deleteById(editor.map(Editor::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return editor.get().getId();
    }

    @Override
    public List<EditorResponseDto> readAll() {
        return collectionEditorResponseConverter.toListDto(editorRepository.findAll());
    }
}
