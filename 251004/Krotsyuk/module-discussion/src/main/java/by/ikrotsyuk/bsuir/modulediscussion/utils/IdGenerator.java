package by.ikrotsyuk.bsuir.modulediscussion.utils;

import by.ikrotsyuk.bsuir.modulediscussion.entity.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IdGenerator {
    private final MongoTemplate mongoTemplate;

    public long generateSequence() {
        Query query = new Query(Criteria.where("_id").is("global_sequence"));
        Update update = new Update().inc("seq", 1);
        DatabaseSequence counter = mongoTemplate.findAndModify(
                query,
                update,
                DatabaseSequence.class
        );

        return (counter != null) ? counter.getSeq() : 1;
    }
}
