package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.User;
import by.bsuir.publisher.dto.requests.UserRequestDto;
import by.bsuir.publisher.dto.requests.converters.UserRequestConverter;
import by.bsuir.publisher.dto.responses.UserResponseDto;
import by.bsuir.publisher.dto.responses.converters.UserResponseConverter;
import by.bsuir.publisher.dto.responses.converters.CollectionUserResponseConverter;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.UserRepository;
import by.bsuir.publisher.services.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRequestConverter userRequestConverter;
    private final UserResponseConverter userResponseConverter;
    private final CollectionUserResponseConverter collectionUserResponseConverter;

    @Override
    @Validated
    public UserResponseDto create(@Valid @NonNull UserRequestDto dto) throws EntityExistsException {
        Optional<User> user = dto.getId() == null ? Optional.empty() : userRepository.findById(dto.getId());
        if (user.isEmpty()) {
            return userResponseConverter.toDto(userRepository.save(userRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<UserResponseDto> read(@NonNull Long id) {
        return userRepository.findById(id).flatMap(user -> Optional.of(
                userResponseConverter.toDto(user)));
    }

    @Override
    @Validated
    public UserResponseDto update(@Valid @NonNull UserRequestDto dto) throws NoEntityExistsException {
        Optional<User> user = dto.getId() == null || userRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(userRequestConverter.fromDto(dto));
        return userResponseConverter.toDto(userRepository.save(user.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<User> user = userRepository.findById(id);
        userRepository.deleteById(user.map(User::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return user.get().getId();
    }

    @Override
    public List<UserResponseDto> readAll() {
        return collectionUserResponseConverter.toListDto(userRepository.findAll());
    }
}
