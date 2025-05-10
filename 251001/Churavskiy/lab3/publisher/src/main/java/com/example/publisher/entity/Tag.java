package com.example.publisher.entity;

import com.example.publisher.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Topic> topics;
}
