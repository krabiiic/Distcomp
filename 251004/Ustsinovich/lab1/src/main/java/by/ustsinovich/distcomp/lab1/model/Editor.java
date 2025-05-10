package by.ustsinovich.distcomp.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Setter
@Getter
@Entity
public class Editor extends AbstractEntity {

    @Length(min = 2, max = 64)
    @Column(unique = true, nullable = false, length = 64)
    private String login;

    @Length(min = 8, max = 128)
    @Column(nullable = false, length = 128)
    private String password;

    @Length(min = 2, max = 64)
    @Column(nullable = false, length = 64)
    private String firstName;

    @Length(min = 2, max = 64)
    @Column(nullable = false, length = 64)
    private String lastName;

    @OneToMany
    private Set<Issue> issues;

}
