package com.example.modulepublisher.service;

import com.example.modulepublisher.dto.AuthorDTO;
import com.example.modulepublisher.entity.Author;
import com.example.modulepublisher.exception.DublExeption;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.AuthorMapper;
import com.example.modulepublisher.repository.AuthorRepository;
import com.example.modulepublisher.repository.redis.AuthorRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorMapper userMapper;
    private final AuthorRepository userRepository;
    private final AuthorRedisRepository userRedisRepository;

    public AuthorDTO createAuthor(AuthorDTO userDTO){
        Author user = userMapper.toAuthor(userDTO);
        Optional<Author> odubl = userRepository.findAuthorByLogin(user.getLogin());
        if(odubl.isPresent()){
            throw new DublExeption("aaaaa");
        }
        userRepository.save(user);
        AuthorDTO dto = userMapper.toAuthorDTO(user);
        userDTO.setId(dto.getId());
        userRedisRepository.save(userDTO);
        return  dto;


    }

    public AuthorDTO deleteAuthor(int id) throws Exception {
        userRedisRepository.deleteById(String.valueOf(id));
        Optional<Author> ouser = userRepository.findAuthorById(id);
        Author user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        AuthorDTO dto = userMapper.toAuthorDTO(user);
        userRepository.delete(user);
        return  dto;


    }

    public AuthorDTO getAuthor(int id){
        Optional<Author> ouser = userRepository.findAuthorById(id);
        Author user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        AuthorDTO dto = userMapper.toAuthorDTO(user);
        return  dto;


    }

    public List<AuthorDTO> getAuthors(){
        List<Author> users = userRepository.findAll();
        List<AuthorDTO> dtos = userMapper.toAuthorDTOList(users);
        return  dtos;


    }

    public AuthorDTO updateAuthor(AuthorDTO userDTO){
        Author user = userMapper.toAuthor(userDTO);
        userRepository.save(user);
        AuthorDTO dto = userMapper.toAuthorDTO(user);
        return  dto;


    }



}
