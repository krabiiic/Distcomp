package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.requests.MarkRequestDto;
import by.bsuir.publisher.dto.requests.converters.MarkRequestConverter;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
import by.bsuir.publisher.dto.responses.converters.CollectionMarkResponseConverter;
import by.bsuir.publisher.dto.responses.converters.MarkResponseConverter;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.MarkRepository;
import by.bsuir.publisher.services.MarkService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(rollbackFor = {EntityExistsException.class, NoEntityExistsException.class})
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;
    private final MarkRequestConverter markRequestConverter;
    private final MarkResponseConverter markResponseConverter;
    private final CollectionMarkResponseConverter collectionMarkResponseConverter;

    @Override
    @Validated
    public MarkResponseDto create(@Valid @NonNull MarkRequestDto dto) throws EntityExistsException {
        Optional<Mark> mark = dto.getId() == null ? Optional.empty() : markRepository.findById(dto.getId());
        if (mark.isEmpty()) {
            return markResponseConverter.toDto(markRepository.save(markRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<MarkResponseDto> read(@NonNull Long id) {
        return markRepository.findById(id).flatMap(mark -> Optional.of(
                markResponseConverter.toDto(mark)));
    }

    @Override
    @Validated
    public MarkResponseDto update(@Valid @NonNull MarkRequestDto dto) throws NoEntityExistsException {
        Optional<Mark> mark = dto.getId() == null || markRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(markRequestConverter.fromDto(dto));
        return markResponseConverter.toDto(markRepository.save(mark.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Mark> mark = markRepository.findById(id);
        markRepository.deleteById(mark.map(Mark::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return mark.get().getId();
    }

    @Override
    public List<MarkResponseDto> readAll() {
        return collectionMarkResponseConverter.toListDto(markRepository.findAll());
    }
}
