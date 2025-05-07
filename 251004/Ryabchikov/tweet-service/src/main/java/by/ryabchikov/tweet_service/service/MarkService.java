package by.ryabchikov.tweet_service.service;

import by.ryabchikov.tweet_service.dto.mark.MarkRequestTo;
import by.ryabchikov.tweet_service.dto.mark.MarkResponseTo;
import by.ryabchikov.tweet_service.dto.mark.MarkUpdateRequestTo;

import java.util.List;

public interface MarkService {
    MarkResponseTo create(MarkRequestTo markRequestTo);

    List<MarkResponseTo> readAll();

    MarkResponseTo readById(Long id);

    MarkResponseTo update(MarkUpdateRequestTo markUpdateRequestTo);

    void deleteById(Long id);
}
