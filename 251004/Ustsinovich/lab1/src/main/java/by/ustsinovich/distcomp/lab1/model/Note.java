package by.ustsinovich.distcomp.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Entity
public class Note extends AbstractEntity {

    @ManyToOne
    private Issue issue;

    @Length(min = 2, max = 2048)
    @Column(length = 2048)
    private String content;

}
