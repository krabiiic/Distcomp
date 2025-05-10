package com.rita.publisher.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_writer")
public class Writer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    @Length(min = 2, max = 64)
    private String login;
    private String password;
    private String firstname;
    private String lastname;

    @OneToMany(//fetch = FetchType.LAZY,
            mappedBy = "writer",
            cascade = CascadeType.ALL)
    private List<Tweet> tweets=new ArrayList<Tweet>();

}
