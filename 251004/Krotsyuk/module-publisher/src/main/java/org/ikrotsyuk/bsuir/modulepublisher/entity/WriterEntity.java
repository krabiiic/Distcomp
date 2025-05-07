package org.ikrotsyuk.bsuir.modulepublisher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_writer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WriterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 64)
    private String login;
    @NotBlank
    @Size(min = 8, max = 128)
    private String password;
    @NotBlank
    @Size(min = 2, max = 64)
    private String firstname;
    @NotBlank
    @Size(min = 2, max = 64)
    private String lastname;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<ArticleEntity> articles = new ArrayList<>();
}
