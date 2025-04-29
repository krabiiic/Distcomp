package com.example.restservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tbl_mark")
@Data
public class Mark implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String name;


    @ManyToMany(mappedBy = "marks", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Article> articles;
}
