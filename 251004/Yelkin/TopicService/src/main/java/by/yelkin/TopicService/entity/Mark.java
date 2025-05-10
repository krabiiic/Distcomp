package by.yelkin.TopicService.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_mark")
public class Mark extends BaseEntity {
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tbl_m2m_topic_mark",
            joinColumns = @JoinColumn(name = "mark_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topics;
}
