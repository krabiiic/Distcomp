package by.kardychka.Publisher.services;

import by.kardychka.Publisher.DTOs.Requests.CreatorRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.CreatorResponseDTO;
import by.kardychka.Publisher.models.Creator;
import by.kardychka.Publisher.repositories.CreatorsRepository;
import by.kardychka.Publisher.utils.exceptions.NotFoundException;
import by.kardychka.Publisher.utils.mappers.CreatorsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreatorsService {
    private final CreatorsRepository creatorsRepository;
    private final CreatorsMapper creatorsMapper;

    @Autowired
    public CreatorsService(CreatorsRepository creatorsRepository, CreatorsMapper creatorsMapper) {
        this.creatorsRepository = creatorsRepository;
        this.creatorsMapper = creatorsMapper;
    }


    @Transactional
    public CreatorResponseDTO save(CreatorRequestDTO creatorRequestDTO) {
        Creator creator = creatorsMapper.toCreator(creatorRequestDTO);
        return creatorsMapper.toCreatorResponse(creatorsRepository.save(creator));
    }

    @Transactional
    public void deleteById(long id) {
        if (!creatorsRepository.existsById(id)) {
            throw new NotFoundException("Creator not found");
        }
        creatorsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CreatorResponseDTO> findAll() {
        return creatorsMapper.toCreatorResponseList(creatorsRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CreatorResponseDTO findById(long id){
        Creator creator = creatorsRepository.findById(id).orElseThrow(() -> new NotFoundException("Creator not found"));
        return creatorsMapper.toCreatorResponse(creator);
    }

    @Transactional
    public CreatorResponseDTO update(long id, CreatorRequestDTO creatorRequestDTO) {
        creatorRequestDTO.setId(id);
        return update(creatorRequestDTO);
    }

    @Transactional
    public CreatorResponseDTO update(CreatorRequestDTO creatorRequestDTO) {
        Creator creator = creatorsMapper.toCreator(creatorRequestDTO);
        if (!creatorsRepository.existsById(creator.getId())) {
            throw new NotFoundException("Creator not found");
        }

        return creatorsMapper.toCreatorResponse(creatorsRepository.save(creator));
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login){
        return creatorsRepository.existsByLogin(login);
    }
}
