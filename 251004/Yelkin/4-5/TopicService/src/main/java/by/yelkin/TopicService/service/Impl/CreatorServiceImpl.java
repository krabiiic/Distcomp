package by.yelkin.TopicService.service.Impl;

import by.yelkin.TopicService.dto.creator.CreatorRequestTo;
import by.yelkin.TopicService.dto.creator.CreatorResponseTo;
import by.yelkin.TopicService.dto.creator.CreatorUpdate;
import by.yelkin.TopicService.entity.Creator;
import by.yelkin.TopicService.mapper.CreatorMapper;
import by.yelkin.TopicService.repository.CreatorRepository;
import by.yelkin.TopicService.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private  CreatorRepository creatorRepository;
    private  CreatorMapper creatorMapper;

    @Autowired
    public CreatorServiceImpl(CreatorRepository creatorRepository, CreatorMapper creatorMapper) {
        this.creatorRepository = creatorRepository;
        this.creatorMapper = creatorMapper;
    }

    @Override
    public CreatorResponseTo create(CreatorRequestTo creatorRequestTo) {
        return creatorMapper.toCreatorResponseTo(creatorRepository.save(creatorMapper.toCreator(creatorRequestTo)));
    }

    @Override
    public CreatorResponseTo update(CreatorUpdate updatedCreator) {
        Creator creator = creatorRepository.findById(updatedCreator.getId())
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));

        if (updatedCreator.getId() != null) {
            creator.setId(updatedCreator.getId());
        }
        if (updatedCreator.getLogin() != null) {
            creator.setLogin(updatedCreator.getLogin());
        }
        if (updatedCreator.getPassword() != null) {
            creator.setPassword(updatedCreator.getPassword());
        }
        if (updatedCreator.getFirstname() != null) {
            creator.setFirstname(updatedCreator.getFirstname());
        }
        if (updatedCreator.getLastname() != null) {
            creator.setLastname(updatedCreator.getLastname());
        }

        return creatorMapper.toCreatorResponseTo(creatorRepository.save(creator));
    }

    @Override
    public void deleteById(Long id) {
        creatorRepository.deleteById(id);
    }

    @Override
    public List<CreatorResponseTo> findAll() {
        return StreamSupport.stream(creatorRepository.findAll().spliterator(), false)
                .map(creatorMapper::toCreatorResponseTo)
                .toList();
    }

    @Override
    public Optional<CreatorResponseTo> findById(Long id) {
        return creatorRepository.findById(id)
                .map(creatorMapper::toCreatorResponseTo);
    }
}
