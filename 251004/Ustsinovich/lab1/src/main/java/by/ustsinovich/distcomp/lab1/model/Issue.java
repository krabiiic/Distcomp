package by.ustsinovich.distcomp.lab1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
public class Issue extends AbstractEntity {

    @ManyToOne
    private Editor editor;

    @Length(min = 2, max = 64)
    @Column(unique = true, nullable = false, length = 64)
    private String title;

    @Length(min = 4, max = 2048)
    @Column(length = 2048)
    private String content;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime modified;

    @ManyToMany
    private Set<Mark> marks;

}
