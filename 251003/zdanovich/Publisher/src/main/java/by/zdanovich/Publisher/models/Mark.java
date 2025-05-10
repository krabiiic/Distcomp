package by.zdanovich.Publisher.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tbl_mark")
@NoArgsConstructor
@Getter
@Setter
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "marks")
    private List<Issue> issues = new ArrayList<>();

    public Mark(String name) {
        this.name = name;
    }
}
