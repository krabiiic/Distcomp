package com.example.publisher.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 64)
    private String login;

    @Column(nullable = false, length = 128)
    private String password;

    @Column(length = 64)
    private String firstname;

    @Column(length = 64)
    private String lastname;
}
