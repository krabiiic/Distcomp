package com.example.JPA2.Impl.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @Column(name = "id", nullable=false, unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "login", nullable=false, unique = true)
    private String login;
    @Column(name = "password")
    private String password;
//    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
}
