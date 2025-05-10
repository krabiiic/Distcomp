package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Creator;
import by.bsuir.publisher.dto.requests.CreatorRequestDto;
import by.bsuir.publisher.dto.requests.converters.CreatorRequestConverter;
import by.bsuir.publisher.dto.responses.CreatorResponseDto;
import by.bsuir.publisher.dto.responses.converters.CreatorResponseConverter;
import by.bsuir.publisher.dto.responses.converters.CollectionCreatorResponseConverter;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.CreatorRepository;
import by.bsuir.publisher.services.CreatorService;
import jakarta.persistence.EntityExistsException;
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
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final CreatorRequestConverter creatorRequestConverter;
    private final CreatorResponseConverter creatorResponseConverter;
    private final CollectionCreatorResponseConverter collectionCreatorResponseConverter;

    @Override
    @Validated
    public CreatorResponseDto create(@Valid @NonNull CreatorRequestDto dto) throws EntityExistsException {
        Optional<Creator> creator = dto.getId() == null ? Optional.empty() : creatorRepository.findById(dto.getId());
        if (creator.isEmpty()) {
            return creatorResponseConverter.toDto(creatorRepository.save(creatorRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<CreatorResponseDto> read(@NonNull Long id) {
        return creatorRepository.findById(id).flatMap(creator -> Optional.of(
                creatorResponseConverter.toDto(creator)));
    }

    @Override
    @Validated
    public CreatorResponseDto update(@Valid @NonNull CreatorRequestDto dto) throws NoEntityExistsException {
        Optional<Creator> creator = dto.getId() == null || creatorRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(creatorRequestConverter.fromDto(dto));
        return creatorResponseConverter.toDto(creatorRepository.save(creator.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Creator> creator = creatorRepository.findById(id);
        creatorRepository.deleteById(creator.map(Creator::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return creator.get().getId();
    }

    @Override
    public List<CreatorResponseDto> readAll() {
        return collectionCreatorResponseConverter.toListDto(creatorRepository.findAll());
    }
}
