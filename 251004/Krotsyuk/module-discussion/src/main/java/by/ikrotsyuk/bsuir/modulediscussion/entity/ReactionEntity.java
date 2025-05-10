package by.ikrotsyuk.bsuir.modulediscussion.entity;


import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tbl_reaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReactionEntity {
    @Id
    private Long id;
    private Long articleId;
    private String country;
    @Size(min = 2, max = 2048)
    private String content;
}
