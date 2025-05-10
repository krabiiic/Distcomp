package by.kopvzakone.distcomp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_issue") //tbl_tweet
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long editorId;
    @Size(min = 2, max = 64)
    String title;
    @Size(min = 4, max = 2048)
    String content;
    Timestamp created;
    Timestamp modified;
    @ManyToMany
    @JoinTable(
            name = "tbl_issue_mark",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "mark_id")
    )
    private Set<Tag> marks;
}
