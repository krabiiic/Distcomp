package by.zdanovich.Discussion.repositories;


import by.zdanovich.Discussion.models.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Message.MessageKey> {
    @Query("SELECT * FROM tbl_message WHERE country = :country AND id = :id")
    Optional<Message> findByCountryAndId(@Param("country") String country, @Param("id") Long id);

    @Query("DELETE FROM tbl_message WHERE country = :country AND id = :id")
    void deleteByCountryAndId(@Param("country") String country, @Param("id") Long id);
}
