package by.ryabchikov.tweet_service.service.impl;

import by.ryabchikov.tweet_service.dto.mark.MarkRequestTo;
import by.ryabchikov.tweet_service.dto.mark.MarkResponseTo;
import by.ryabchikov.tweet_service.dto.mark.MarkUpdateRequestTo;
import by.ryabchikov.tweet_service.entity.Mark;
import by.ryabchikov.tweet_service.exception.MarkNotFoundException;
import by.ryabchikov.tweet_service.mapper.MarkMapper;
import by.ryabchikov.tweet_service.repository.MarkRepository;
import by.ryabchikov.tweet_service.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final MarkMapper markMapper;

    @Override
    @Transactional
    public MarkResponseTo create(MarkRequestTo markRequestTo) {
        return markMapper.toMarkResponseTo(
                markRepository.save(markMapper.toMark(markRequestTo))
        );
    }

    @Override
    @Transactional
    public List<MarkResponseTo> readAll() {
        return markRepository.findAll().stream()
                .map(markMapper::toMarkResponseTo)
                .toList();
    }

    @Override
    @Transactional
    public MarkResponseTo readById(Long id) {
        return markMapper.toMarkResponseTo(
                markRepository.findById(id).orElseThrow(() -> MarkNotFoundException.byId(id))
        );
    }

    @Override
    @Transactional
    public MarkResponseTo update(MarkUpdateRequestTo markUpdateRequestTo) {
        long markId = markUpdateRequestTo.id();
        Mark mark = markRepository.findById(markId)
                .orElseThrow(() -> MarkNotFoundException.byId(markId));

        mark.setName(markUpdateRequestTo.name());

        return markMapper.toMarkResponseTo(mark);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        markRepository.findById(id)
                .orElseThrow(() -> MarkNotFoundException.byId(id));

        markRepository.deleteById(id);
    }
}
