package by.ryabchikov.tweet_service.mapper;

import by.ryabchikov.tweet_service.dto.mark.MarkRequestTo;
import by.ryabchikov.tweet_service.dto.mark.MarkResponseTo;
import by.ryabchikov.tweet_service.entity.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {

    MarkResponseTo toMarkResponseTo(Mark mark);

//    @Mapping(target = "id", ignore = true)
    Mark toMark(MarkRequestTo markRequestTo);
}
