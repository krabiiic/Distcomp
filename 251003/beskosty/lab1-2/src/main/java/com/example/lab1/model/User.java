package com.example.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    @Size(min = 2, max = 64)
    private String login;

    @Column(nullable = false, length = 128)
    @Size(min = 8, max = 128)
    private String password;

    @Column(nullable = false, length = 64)
    @Size(min = 2, max = 64)
    private String firstname;

    @Column(nullable = false, length = 64)
    @Size(min = 2, max = 64)
    private String lastname;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
}
