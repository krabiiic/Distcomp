package by.kapinskiy.Publisher.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tbl_tag")
@NoArgsConstructor
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Issue> issues = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }
}
