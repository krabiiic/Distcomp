package by.kapinskiy.Publisher.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_issue")
@Data
public class Issue {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_issue_tag", joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

}
