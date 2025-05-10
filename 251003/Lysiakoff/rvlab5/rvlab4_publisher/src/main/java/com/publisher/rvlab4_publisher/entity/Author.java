package com.publisher.rvlab4_publisher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 64, unique = true)
    private String login;
    @Column(nullable = false, length = 128)
    private String password;
    @Column(nullable = false, length = 64)
    private String firstname;
    @Column(nullable = false, length = 64)
    private String lastname;
    private List<Long> newsIds;

    public Author(Long id, String login, String password, String firstname, String lastname) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.newsIds = newsIds;
    }
}
