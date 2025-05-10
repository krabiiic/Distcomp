package by.ikrotsyuk.bsuir.modulediscussion.repository;

import by.ikrotsyuk.bsuir.modulediscussion.entity.ReactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends MongoRepository<ReactionEntity, Long> {
}
