package by.kapinskiy.Publisher.services;

import by.kapinskiy.Publisher.DTOs.Requests.UserRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.UserResponseDTO;
import by.kapinskiy.Publisher.models.User;
import by.kapinskiy.Publisher.repositories.UsersRepository;
import by.kapinskiy.Publisher.utils.exceptions.NotFoundException;
import by.kapinskiy.Publisher.utils.mappers.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        User user = usersMapper.toUser(userRequestDTO);
        return usersMapper.toUserResponse(usersRepository.save(user));
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteById(long id) {
        if (!usersRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        usersRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return usersMapper.toUserResponseList(usersRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO findById(long id){
        User user = usersRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return usersMapper.toUserResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserResponseDTO update(long id, UserRequestDTO userRequestDTO) {
        userRequestDTO.setId(id);
        return update(userRequestDTO);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userRequestDTO.id")
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        User user = usersMapper.toUser(userRequestDTO);
        if (!usersRepository.existsById(user.getId())) {
            throw new NotFoundException("User not found");
        }

        return usersMapper.toUserResponse(usersRepository.save(user));
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login){
        return usersRepository.existsByLogin(login);
    }
}
