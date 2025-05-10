package by.bsuir.publisher.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@RedisHash("comment")
public class Comment implements Serializable {
    @Id
    private Long id;
    private Long topicId;
    private String content;
}
