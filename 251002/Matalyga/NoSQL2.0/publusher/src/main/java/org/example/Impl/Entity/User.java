package org.example.Impl.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
