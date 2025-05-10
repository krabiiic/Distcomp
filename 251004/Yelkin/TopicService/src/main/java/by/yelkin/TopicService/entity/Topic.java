package by.yelkin.TopicService.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_topic")
public class Topic extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    private String title;

    private String content;

    @ManyToMany(mappedBy = "topics", cascade = CascadeType.ALL)
    private List<Mark> marks;

    public void setMarks(List<Mark> marks) {
        if (Objects.nonNull(marks)) {
            marks.forEach(mark -> {
                if (Objects.isNull(mark.getTopics())) {
                    mark.setTopics(new ArrayList<>());
                }
                mark.getTopics().add(this);
            });
        }
        this.marks = marks;
    }
}
