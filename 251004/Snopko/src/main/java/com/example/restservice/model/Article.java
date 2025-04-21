package com.example.restservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_article")
@Data
public class Article implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false,  unique = true, length = 64)
    private String title;

    @Column(length = 2048)
    private String content;

    private LocalDateTime created;
    private LocalDateTime modified;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "tbl_article_mark",
            schema = "distcomp",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "mark_id")
    )

    private List<Mark> marks;
}
