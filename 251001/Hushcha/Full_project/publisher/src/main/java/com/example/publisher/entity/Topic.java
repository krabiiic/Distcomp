package com.example.publisher.entity;

import com.example.publisher.entity.BaseEntity;
import com.example.publisher.entity.Tag;
import com.example.publisher.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_news", uniqueConstraints = {
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
            name = "tbl_news_mark",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "mark_id")
    )
    private Set<Tag> tags;
}
