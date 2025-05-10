package com.example.lab1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_topic", uniqueConstraints = {
        @UniqueConstraint(columnNames = "title")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Topic implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(length = 2048)
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime created;

    private LocalDateTime modified;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "tbl_topic_tag",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
