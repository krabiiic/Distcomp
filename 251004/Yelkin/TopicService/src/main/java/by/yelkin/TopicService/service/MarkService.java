package by.yelkin.TopicService.service;

import by.yelkin.TopicService.dto.mark.MarkRq;
import by.yelkin.TopicService.dto.mark.MarkRs;
import by.yelkin.TopicService.dto.mark.MarkUpdateRq;
import by.yelkin.TopicService.mapping.MarkMapper;
import by.yelkin.TopicService.repository.MarkRepository;
import by.yelkin.apihandler.exception.ApiError;
import by.yelkin.apihandler.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;
    private final MarkMapper markMapper;

    @Transactional
    public MarkRs create(MarkRq rq) {
        return markMapper.toDto(markRepository.save(markMapper.fromDto(rq)));
    }

    @Transactional
    public MarkRs readById(Long id) {
        return markMapper.toDto(markRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiError.ERR_MARK_NOT_FOUND, id.toString())));
    }

    @Transactional
    public List<MarkRs> readAll() {
        return markMapper.toDtoList(markRepository.findAll());
    }

    @Transactional
    public MarkRs update(MarkUpdateRq rq) {
        var mark = markRepository.findById(rq.getId())
                .orElseThrow(() -> new ApiException(ApiError.ERR_MARK_NOT_FOUND, rq.getId().toString()));

        markMapper.updateMark(mark, rq);

        return markMapper.toDto(markRepository.save(mark));
    }

    @Transactional
    public void deleteById(Long id) {
        if (markRepository.findById(id).isEmpty()) {
            throw new ApiException(ApiError.ERR_MARK_NOT_FOUND, id.toString());
        }
        markRepository.deleteById(id);
    }
}
