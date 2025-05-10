/*package org.ikrotsyuk.bsuir.firstservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_reaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;
    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;
}*/
