package by.ryabchikov.tweet_service.service;

import by.ryabchikov.tweet_service.dto.creator.CreatorRequestTo;
import by.ryabchikov.tweet_service.dto.creator.CreatorResponseTo;
import by.ryabchikov.tweet_service.dto.creator.CreatorUpdateRequestTo;

import java.util.List;

public interface CreatorService {
    CreatorResponseTo create(CreatorRequestTo creatorRequestTo);

    List<CreatorResponseTo> readAll();

    CreatorResponseTo readById(Long id);

    CreatorResponseTo update(CreatorUpdateRequestTo creatorUpdateRequestTo);

    void deleteById(Long id);
}
