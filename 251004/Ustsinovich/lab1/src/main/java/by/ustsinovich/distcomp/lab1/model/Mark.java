package by.ustsinovich.distcomp.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Setter
@Getter
@Entity
public class Mark extends AbstractEntity {

    @Length(min = 2, max = 32)
    @Column(unique = true, nullable = false, length = 32)
    private String name;

    @ManyToMany
    private Set<Issue> issues;

}
