package by.yelkin.TopicService.service;

import by.yelkin.TopicService.dto.creator.CreatorRq;
import by.yelkin.TopicService.dto.creator.CreatorRs;
import by.yelkin.TopicService.dto.creator.CreatorUpdateRq;
import by.yelkin.TopicService.mapping.CreatorMapper;
import by.yelkin.TopicService.repository.CreatorRepository;
import by.yelkin.apihandler.exception.ApiError;
import by.yelkin.apihandler.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorRepository creatorRepository;
    private final CreatorMapper creatorMapper;

    @Transactional
    public CreatorRs create(CreatorRq rq) {
        if (creatorRepository.findByLogin(rq.getLogin()).isPresent()) {
            throw new ApiException(ApiError.ERR_NO_RIGHTS);
        }
        return creatorMapper.toDto(creatorRepository.save(creatorMapper.fromDto(rq)));
    }

    @Transactional
    public CreatorRs readById(Long id) {
        return creatorMapper.toDto(creatorRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiError.ERR_CREATOR_NOT_FOUND, id.toString())));
    }

    @Transactional
    public List<CreatorRs> readAll() {
        return creatorMapper.toDtoList(creatorRepository.findAll());
    }

    @Transactional
    public CreatorRs update(CreatorUpdateRq rq) {
        var creator = creatorRepository.findById(rq.getId())
                .orElseThrow(() -> new ApiException(ApiError.ERR_CREATOR_NOT_FOUND, rq.getId().toString()));

        creatorMapper.updateCreator(creator, rq);

        return creatorMapper.toDto(creatorRepository.save(creator));
    }

    @Transactional
    public void deleteById(Long id) {
        if (creatorRepository.findById(id).isEmpty()) {
            throw new ApiException(ApiError.ERR_CREATOR_NOT_FOUND, id.toString());
        }
        creatorRepository.deleteById(id);
    }
}
