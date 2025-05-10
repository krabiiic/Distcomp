package com.example.lab1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tbl_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 64)
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 2, max = 64)
    @Column(nullable = false)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 64)
    @Column(nullable = false)
    private String lastname;
}
